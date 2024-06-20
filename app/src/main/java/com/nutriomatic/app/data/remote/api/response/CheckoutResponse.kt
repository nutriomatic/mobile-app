package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class CheckoutResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("transactions")
    val transactions: List<TransactionsItem> = emptyList(),

    @field:SerializedName("status")
    val status: String
)

data class TransactionsItem(

    @field:SerializedName("tsc_price")
    val tscPrice: Int,

    @field:SerializedName("store_id")
    val storeId: String,

    @field:SerializedName("createdAt")
    val createdAt: String,

    @field:SerializedName("tsc_id")
    val tscId: String,

    @field:SerializedName("payment_id")
    val paymentId: String,

    @field:SerializedName("product_id")
    var productId: String,

    @field:SerializedName("tsc_bukti")
    var tscBukti: String,

    @field:SerializedName("tsc_virtualaccount")
    val tscVirtualaccount: String,

    @field:SerializedName("tsc_status")
    val tscStatus: String,

    @field:SerializedName("tsc_start")
    val tscStart: String,

    @field:SerializedName("tsc_end")
    val tscEnd: String,

    @field:SerializedName("updatedAt")
    val updatedAt: String
)
