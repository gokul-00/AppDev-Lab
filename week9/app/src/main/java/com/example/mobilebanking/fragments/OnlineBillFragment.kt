package com.example.mobilebanking.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.mobilebanking.databinding.FragmentOnlineBillBinding
import com.example.mobilebanking.helpers.viewLifecycle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OnlineBillFragment : Fragment() {
    var binding by viewLifecycle<FragmentOnlineBillBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnlineBillBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpinner()
        binding.buttonPay.setOnClickListener {
            val type = binding.spinnerBillType.selectedItem.toString()
            val accountNumber = binding.textInputBeneficiaryAccount.editText?.text.toString()
            val amount = binding.textInputAmount.editText?.text.toString().toInt()
            CoroutineScope(Dispatchers.IO).launch {
                payBill(type, accountNumber, amount)
            }
        }
    }

    private suspend fun payBill(type: String, accountNumber: String, amount: Int) {
        val user = Firebase.auth.currentUser!!
        val userMobileNumber = user.phoneNumber!!.removePrefix("+91")
        val transaction = hashMapOf(
            "type" to type,
            "amount" to amount,
            "forAccountNumber" to accountNumber,
            "senderMobileNumber" to userMobileNumber
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
        Firebase.firestore.collection("users").document(userMobileNumber).update("balance", userBalance - amount).await()
        Firebase.firestore.collection("users").document(userMobileNumber).collection("transactions")
            .add(transaction)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Transaction successful", Toast.LENGTH_SHORT).show()
                Log.d("ExternalFundFragment", "Transaction added with ID: ${it.id}")
            }
            .addOnFailureListener {
                Log.w("ExternalFundFragment", "Error adding transaction", it)
            }
    }

    private fun setupSpinner() {
        val typeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arrayOf("Electricity", "Water", "Mobile", "Internet")
        )
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerBillType.adapter = typeAdapter
        binding.spinnerBillType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (parent?.getItemAtPosition(position).toString()) {
                    "Electricity" -> {
                        binding.textInputBeneficiaryAccount.hint = "Electricity Number"
                    }
                    "Water" -> {
                        binding.textInputBeneficiaryAccount.hint = "Water Number"
                    }
                    "Mobile" -> {
                        binding.textInputBeneficiaryAccount.hint = "Mobile Number"
                    }
                    "Internet" -> {
                        binding.textInputBeneficiaryAccount.hint = "Internet Number"
                    }
                }
            }
        }
    }
}