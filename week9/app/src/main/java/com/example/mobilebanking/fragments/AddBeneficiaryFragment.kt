package com.example.mobilebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.mobilebanking.databinding.FragmentAddBeneficiaryBinding
import com.example.mobilebanking.helpers.viewLifecycle
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddBeneficiaryFragment : Fragment() {
    var binding by viewLifecycle<FragmentAddBeneficiaryBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBeneficiaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private suspend fun doesAccountExists(mobileNumber: String): Boolean {
        val snapshot = Firebase.firestore.collection("users").document(mobileNumber).get().await()
        return snapshot.exists()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonAddBeneficiary.setOnClickListener {
            val nickName = binding.textInputNickName.editText?.text.toString()
            val mobileNumber = binding.textInputMobileNumber.editText?.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                if (!doesAccountExists(mobileNumber)) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        addBeneficiary(nickName, mobileNumber)
                    }
                }
            }
        }
    }

    private fun addBeneficiary(nickName: String, mobileNumber: String) {
        Firebase.firestore.collection("users")
            .document(Firebase.auth.currentUser!!.phoneNumber!!.removePrefix("+91")).collection("beneficiaries")
            .document(mobileNumber)
            .set(mapOf("nickName" to nickName, "mobileNumber" to mobileNumber))
            .addOnCompleteListener {
                Toast.makeText(context, "Beneficiary added!", Toast.LENGTH_SHORT).show()
            }
    }
}