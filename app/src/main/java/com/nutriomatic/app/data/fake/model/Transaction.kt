package com.nutriomatic.app.data.fake.model

data class Transaction(
    val id: String,
    val price: Double,
    val va: String,
    val startDate: String,
    val endDate: String,
    val status: String,
    val paymentProof: String,
    val createdAt: String,
    val updatedAt: String,
)