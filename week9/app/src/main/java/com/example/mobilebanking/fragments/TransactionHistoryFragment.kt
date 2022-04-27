package com.example.mobilebanking.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobilebanking.adapters.TransactionAdapter
import com.example.mobilebanking.databinding.FragmentTransactionHistoryBinding
import com.example.mobilebanking.helpers.viewLifecycle
import com.example.mobilebanking.models.Transaction
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TransactionHistoryFragment : Fragment() {

    private var binding by viewLifecycle<FragmentTransactionHistoryBinding>()
    private var task: Task<QuerySnapshot>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewTransactionHistory.adapter = TransactionAdapter()
        getTransactions()
    }

    private fun getTransactions() {
        val transactions = mutableListOf<Transaction>()
        task = Firebase.firestore.collection("users").document(Firebase.auth.currentUser!!.phoneNumber!!.removePrefix("+91")).collection("transactions")
            .get()
            .addOnSuccessListener {
                for (document in it) {
                    transactions.add(document.toObject(Transaction::class.java))
                }
                (binding.recyclerViewTransactionHistory.adapter as TransactionAdapter).submitList(transactions)
            }
    }

    override fun onStop() {
        super.onStop()
    }
}