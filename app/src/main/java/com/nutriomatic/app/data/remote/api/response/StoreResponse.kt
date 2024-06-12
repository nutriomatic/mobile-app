package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class StoreResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("store")
	val store: Store,

	@field:SerializedName("status")
	val status: String
)

data class Store(

	@field:SerializedName("store_id")
	val storeId: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("store_username")
	val storeUsername: String,

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("store_name")
	val storeName: String,

	@field:SerializedName("store_address")
	val storeAddress: String,

	@field:SerializedName("store_contact")
	val storeContact: String,

	@field:SerializedName("updatedAt")
	val updatedAt: String
)
