package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)
