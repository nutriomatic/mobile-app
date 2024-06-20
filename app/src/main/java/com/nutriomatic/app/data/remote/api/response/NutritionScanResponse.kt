package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class NutritionScan(

    @field:SerializedName("sn_grade")
    val snGrade: String,

    @field:SerializedName("sn_salt")
    val snSalt: Double,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("sn_energy")
    val snEnergy: Double,

    @field:SerializedName("sn_fat")
    val snFat: Double,

    @field:SerializedName("sn_sugar")
    val snSugar: Double,

    @field:SerializedName("sn_carbohydrate")
    val snCarbohydrate: Double,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("sn_protein")
    val snProtein: Double,

    @field:SerializedName("user_id")
    val userId: String,

    @field:SerializedName("sn_id")
    val snId: String,

    @field:SerializedName("sn_productType")
    val snProductType: String,

    @field:SerializedName("sn_productName")
    val snProductName: String,

    @field:SerializedName("sn_info")
    val snInfo: String,

    @field:SerializedName("sn_picture")
    val snPicture: String,
)

data class GetNutritionScanByIdResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val nutritionScan: NutritionScan,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("status")
    val status: String,
)

data class GetNutritionScanByUserIdResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("datas")
    val nutritionScans: List<NutritionScan> = emptyList(),

    @field:SerializedName("status")
    val status: String,
)