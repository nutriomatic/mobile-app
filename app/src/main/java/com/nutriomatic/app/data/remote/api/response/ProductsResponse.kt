package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class ProductByIdResponse(

    @field:SerializedName("product")
    val product: ProductsItem,

    @field:SerializedName("status")
    val status: String,
)

data class ProductsResponse(

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("products")
    val products: List<ProductsItem> = emptyList(),

    @field:SerializedName("status")
    val status: String,
)

data class ProductAdvertiseResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("pagination")
    val pagination: Pagination,

    @field:SerializedName("products")
    val products: List<ProductsItem> = emptyList(),

    @field:SerializedName("status")
    val status: String,
)

data class ProductsItem(

    @field:SerializedName("store_id")
    val storeId: String,

    @field:SerializedName("product_energi")
    val productEnergi: Double,

    @field:SerializedName("product_gula")
    val productGula: Double,

    @field:SerializedName("product_fiber")
    val productFiber: Double,

    @field:SerializedName("product_saturatedfat")
    val productLemakJenuh: Double,

    @field:SerializedName("pt_type")
    val ptCode: Int,

    @field:SerializedName("product_lemaktotal")
    val productLemaktotal: Double,

    @field:SerializedName("product_isshow")
    val productIsshow: Int,

    @field:SerializedName("product_servingsize")
    val productServingsize: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("product_price")
    val productPrice: Double,

    @field:SerializedName("product_picture")
    val productPicture: String,

    @field:SerializedName("product_name")
    val productName: String,

    @field:SerializedName("product_karbohidrat")
    val productKarbohidrat: Double,

    @field:SerializedName("product_desc")
    val productDesc: String,

    @field:SerializedName("product_grade")
    val productGrade: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("pt_id")
    val ptId: String,

    @field:SerializedName("product_id")
    val productId: String,

    @field:SerializedName("product_protein")
    val productProtein: Double,

    @field:SerializedName("product_expshow")
    val productExpshow: String,

    @field:SerializedName("product_garam")
    val productGaram: Double,
)

data class CreateProductResponse(
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("status")
    val status: Boolean,
)
