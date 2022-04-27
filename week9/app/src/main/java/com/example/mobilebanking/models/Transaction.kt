package com.example.mobilebanking.models

import java.util.*

data class Transaction(
    val amount: Long = 0,
    val forAccountNumber: String = "",
    val senderMobileNumber: String = "",
    val type: String = "",
    val beneficiaryMobileNumber: String = "",
    val date: Date = Date(),
)
