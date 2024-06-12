package com.nutriomatic.app.data.remote.api.request

import com.google.gson.annotations.SerializedName

data class StoreRequest(
    @SerializedName("store_name")
    val storeName: String,

    @SerializedName("store_username")
    val storeUsername: String,

    @SerializedName("store_address")
    val storeAddress: String,

    @SerializedName("store_contact")
    val storeContact: String,
)