package com.nutriomatic.app.data.remote.api.request

import com.google.gson.annotations.SerializedName
import retrofit2.http.Multipart
import java.io.File

data class CreateProductRequest (
    val product_name: String,
    val product_price: Double,
    val product_desc: String,
    val product_isshow: Boolean = false,
    val product_lemaktotal: Double,
    val product_protein: Double,
    val product_karbohidrat: Double,
    val product_garam: Double,
    val product_grade: String = "Z",
    val productServingsize: Int,
    val pt_name: String,
    @SerializedName("file") val productPicture: File,
)