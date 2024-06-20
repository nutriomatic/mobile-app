package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("user")
	val user: User,

	@field:SerializedName("status")
	val status: String
)

data class UpdateProfileResponse(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class User(

	@field:SerializedName("weight_goal")
	val weightGoal: Int,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("telp")
	val telp: String,

	@field:SerializedName("birthdate")
	val birthdate: String,

	@field:SerializedName("gender")
	val gender: Int,

	@field:SerializedName("hg_desc")
	val hgDesc: String,

	@field:SerializedName("hg_type")
	val hgType: Int,

	@field:SerializedName("al_id")
	val alId: String,

	@field:SerializedName("al_value")
	val alValue: Double,

	@field:SerializedName("weight")
	val weight: Int,

	@field:SerializedName("hg_id")
	val hgId: String,

	@field:SerializedName("al_desc")
	val alDesc: String,

	@field:SerializedName("al_type")
	val alType: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("place")
	val place: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("profpic")
	val profpic: String,

	@field:SerializedName("height")
	val height: Double
)
