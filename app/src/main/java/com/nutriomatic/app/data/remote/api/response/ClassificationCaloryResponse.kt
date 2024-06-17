package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class ClassificationCaloryResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("calories")
	val calories: Int,

	@field:SerializedName("classification")
	val classification: Int,

	@field:SerializedName("status")
	val status: String
)
