package com.nutriomatic.app.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class UpdateTransactionRequest(
    @SerializedName("status")
    val status: String,
)