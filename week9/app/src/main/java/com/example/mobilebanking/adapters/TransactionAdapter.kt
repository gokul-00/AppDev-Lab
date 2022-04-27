package com.example.mobilebanking.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mobilebanking.R
import com.example.mobilebanking.databinding.TransactionHistoryItemBinding
import com.example.mobilebanking.models.Transaction

class TransactionAdapter: ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(Companion) {
    inner class TransactionViewHolder(val binding: TransactionHistoryItemBinding): RecyclerView.ViewHolder(binding.root)

    companion object: DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem // TODO: Implement this
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        return TransactionViewHolder(
            TransactionHistoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = currentList[position]
        holder.binding.apply {
            val imageResource = when (transaction.type) {
                "external" -> R.drawable.ic_person
                "Water" -> R.drawable.ic_water
                "Electricity" -> R.drawable.ic_bolt
                "Internet" -> R.drawable.ic_wifi
                "Mobile" -> R.drawable.ic_phone
                else -> R.drawable.ic_launcher_background
            }
            imageView.setImageResource(imageResource)
            textViewAmount.text = "Rs. ${transaction.amount}"
            textViewToNumber.text = "To: ${if(transaction.type == "external") transaction.beneficiaryMobileNumber else transaction.forAccountNumber}"
            textViewFromNumber.text = "From: ${transaction.senderMobileNumber}"
        }
    }
}