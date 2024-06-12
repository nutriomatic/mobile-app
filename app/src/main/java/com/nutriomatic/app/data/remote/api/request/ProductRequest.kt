package com.nutriomatic.app.data.remote.api.request

import com.google.gson.annotations.SerializedName
import java.io.File

data class CreateProductRequest (
    @SerializedName("product_name")
    val productName: String,

    @SerializedName("product_price")
    val productPrice: Double,

    @SerializedName("product_desc")
    val productDesc: String,

    @SerializedName("product_isshow")
    val productIsShow: Boolean = false,

    @SerializedName("product_lemaktotal")
    val productLemakTotal: Double,

    @SerializedName("product_protein")
    val productProtein: Double,

    @SerializedName("product_karbohidrat")
    val productKarbohidrat: Double,

    @SerializedName("product_garam")
    val productGaram: Double,

    @SerializedName("product_grade")
    val productGrade: String = "Z",

    @SerializedName("product_servingsize")
    val productServingSize: Int,

    @SerializedName("pt_name")
    val ptName: String,

    @SerializedName("file")
    val productPicture: File,
)