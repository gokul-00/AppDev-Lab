package com.example.mobilebanking.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.mobilebanking.databinding.FragmentHomeBinding
import com.example.mobilebanking.helpers.viewLifecycle
import com.example.mobilebanking.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private var binding by viewLifecycle<FragmentHomeBinding>()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            val controller = findNavController()
            cardAddBeneficiary.setOnClickListener {
                controller.navigate(HomeFragmentDirections.actionHomeFragmentToAddBeneficiaryFragment())
            }
            cardExternalFundTransfer.setOnClickListener {
                controller.navigate(HomeFragmentDirections.actionHomeFragmentToExternalFundFragment())
            }
            cardOnlineBillPayment.setOnClickListener {
                controller.navigate(HomeFragmentDirections.actionHomeFragmentToOnlineBillFragment())
            }
            cardTransactionHistory.setOnClickListener {
                controller.navigate(HomeFragmentDirections.actionHomeFragmentToTransactionHistoryFragment())
            }
            fabLogout.setOnClickListener {
                Firebase.auth.signOut()
                controller.navigate(HomeFragmentDirections.actionHomeFragmentToSignInFragment())
            }
        }
    }

    override fun onStop() {
        super.onStop()
        listenerRegistration?.remove()
    }

    override fun onStart() {
        super.onStart()
        listenToUserChanges()
    }

    private fun listenToUserChanges() {
        val mobileNumber = Firebase.auth.currentUser?.phoneNumber?.removePrefix("+91")!!
        Log.i("HomeFragment", mobileNumber)
        listenerRegistration = Firebase.firestore.collection("users").document(mobileNumber)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("HomeFragment", exception.message!!)
                } else {
                    val user = snapshot?.toObject(User::class.java)
                    binding.apply {
                        textViewMobileNumber.text = user?.mobileNumber
                        textViewName.text = user?.name
                        textViewBalance.text = "₹ ${user?.balance.toString()}"
                    }
                }
            }
    }
}