package com.example.mobilebanking.fragments

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mobilebanking.databinding.FragmentRegisterBinding
import com.example.mobilebanking.helpers.viewLifecycle
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class RegisterFragment : Fragment() {
    var binding by viewLifecycle<FragmentRegisterBinding>()
    private var hasSentOtp = false
    private var mVerificationId: String? = null
    private var mResendToken: PhoneAuthProvider.ForceResendingToken? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEvents()
    }

    override fun onStart() {
        super.onStart()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToSignInFragment())
        }
    }

    private suspend fun isPhoneAlreadyRegistered(): Boolean  {
        val mobileNumber = binding.textMobileNumber.editText?.text.toString()
        val db = Firebase.firestore
        val snapshot = db.collection("users")
            .whereEqualTo("mobileNumber", mobileNumber)
            .get()
            .await()
        return !snapshot.isEmpty
    }

    private fun setupEvents() {
        binding.buttonRegister.setOnClickListener {
            if (hasSentOtp) {
                verifyOtp()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    if (isPhoneAlreadyRegistered()) {
                        withContext(Dispatchers.Main) {
                            showErrorMessage("Phone number already registered")
                        }
                        return@launch
                    } else {
                        withContext(Dispatchers.Main) {
                            sendOtp()
                        }
                    }
                }
            }
        }

        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToSignInFragment())
        }
    }

    private fun verifyOtp() {
        val otp = binding.textOtp.editText?.text.toString()
        if (otp.length != 6) {
            Toast.makeText(context, "Please enter a valid OTP", Toast.LENGTH_SHORT).show()
            return
        }
        val credential = PhoneAuthProvider.getCredential(mVerificationId!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun showErrorMessage(message: String) {
        binding.textViewError.text = message
    }

    private fun showOtpEditText() {
        hasSentOtp = true
        binding.textMobileNumber.isEnabled = false
        binding.textOtp.visibility = View.VISIBLE
        binding.buttonRegister.text = "Verify"
    }

    private fun sendOtp() {
        val phoneNumber = binding.textMobileNumber.editText?.text.toString()
        if (phoneNumber.length != 10) {
            showErrorMessage("Please enter a valid phone number")
            return
        }
        val callbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }
            }

            override fun onCodeSent(verificationString: String, token: PhoneAuthProvider.ForceResendingToken) {
                mVerificationId = verificationString
                mResendToken = token
                showOtpEditText()
            }
        }

        val auth = FirebaseAuth.getInstance()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91${phoneNumber}")
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(activity as Activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeUserDataInStore()
                    Toast.makeText(context, "Successfully signed in", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToSignInFragment())
                } else {
                    Toast.makeText(context, "Failed to sign in", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun storeUserDataInStore() {
        val mobileNumber = binding.textMobileNumber.editText?.text.toString()
        val name = binding.textName.editText?.text.toString()
        val storeRef = Firebase.firestore.collection("users").document(mobileNumber)
        storeRef.set(mapOf("name" to name, "balance" to 1000, "mobileNumber" to mobileNumber))
    }
}