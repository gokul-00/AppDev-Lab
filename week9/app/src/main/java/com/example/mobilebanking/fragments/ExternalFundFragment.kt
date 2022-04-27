package com.example.mobilebanking.fragments

import android.R
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import com.example.mobilebanking.databinding.FragmentExternalFundBinding
import com.example.mobilebanking.helpers.viewLifecycle
import com.example.mobilebanking.models.Beneficiary
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class ExternalFundFragment : Fragment() {
    var binding by viewLifecycle<FragmentExternalFundBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExternalFundBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getBeneficiaries() {
        val beneficiaries = mutableListOf<String>()
        Firebase.firestore.collection("users").document(Firebase.auth.currentUser!!.phoneNumber!!.removePrefix("+91")).collection("beneficiaries")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    val beneficiary = document.toObject(Beneficiary::class.java)
                    beneficiaries.add("${beneficiary.mobileNumber} - ${beneficiary.nickName}")
                }
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.simple_list_item_1,
                    beneficiaries
                )
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                (binding.dropdownBeneficiaries.editText as? AutoCompleteTextView?
                )?.setAdapter(adapter)
            }
    }

    private suspend fun transferAmount(beneficiary: String, amount: Int) {
        val beneficiaryMobileNumber = beneficiary.split("-")[0].trim()
        val user = Firebase.auth.currentUser!!
        val userMobileNumber = user.phoneNumber!!.removePrefix("+91")
        val date = Date()
        val transaction = hashMapOf(
            "type" to "external",
            "amount" to amount,
            "beneficiaryMobileNumber" to beneficiaryMobileNumber,
            "senderMobileNumber" to userMobileNumber,
            "date" to date
        )
        val userDocument = Firebase.firestore.collection("users").document(userMobileNumber).get().await()
        val userBalance = userDocument.data!!["balance"] as Long
        if (userBalance < amount) {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Insufficient balance", Toast.LENGTH_SHORT).show()
            }
            Log.d("ExternalFundFragment", "Insufficient balance")
            return
        }
        val beneficiaryDocument = Firebase.firestore.collection("users").document(beneficiaryMobileNumber).get().await()
        val beneficiaryBalance = beneficiaryDocument.data!!["balance"] as Long
        Firebase.firestore.collection("users").document(userMobileNumber).update("balance", userBalance - amount).await()
        Firebase.firestore.collection("users").document(beneficiaryMobileNumber).update("balance", beneficiaryBalance + amount).await()
        val newTransaction = Firebase.firestore.collection("users").document(beneficiaryMobileNumber).collection("transactions")
            .add(transaction)
            .await()
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), "Transaction Id: ${newTransaction.id}", Toast.LENGTH_SHORT).show()
        }
        Firebase.firestore.collection("users").document(userMobileNumber).collection("transactions")
            .document(newTransaction.id)
            .set(transaction)
            .await()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBeneficiaries()
        binding.buttonTransfer.setOnClickListener {
            val beneficiary = (binding.dropdownBeneficiaries.editText!! as AutoCompleteTextView).text.toString()
            if (beneficiary.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a beneficiary", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(requireContext(), beneficiary, Toast.LENGTH_SHORT).show()
            val amount = binding.textInputAmount.editText?.text.toString().toInt()
            CoroutineScope(Dispatchers.IO).launch {
                transferAmount(beneficiary, amount)
            }
        }
    }
}