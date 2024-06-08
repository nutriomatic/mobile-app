package com.nutriomatic.app.data.remote.api.response

import com.google.gson.annotations.SerializedName

data class ProductsResponse(

	@field:SerializedName("pagination")
	val pagination: Pagination,

	@field:SerializedName("products")
	val products: List<ProductsItem> = emptyList(),

	@field:SerializedName("status")
	val status: String
)

data class ProductsItem(

	@field:SerializedName("store_id")
	val storeId: String,

	@field:SerializedName("product_lemaktotal")
	val productLemaktotal: Int,

	@field:SerializedName("product_isshow")
	val productIsshow: Boolean,

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
	val productKarbohidrat: Int,

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
	val productProtein: Int,

	@field:SerializedName("product_expshow")
	val productExpshow: String,

	@field:SerializedName("product_garam")
	val productGaram: Int
)

data class Pagination(

	@field:SerializedName("TotalPage")
	val totalPage: Int,

	@field:SerializedName("RecordPerPage")
	val recordPerPage: Int,

	@field:SerializedName("CurrentPage")
	val currentPage: Int,

	@field:SerializedName("Next")
	val next: Int,

	@field:SerializedName("Previous")
	val previous: Int
)
