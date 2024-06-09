package com.nutriomatic.app.data.remote.api.retrofit

import com.nutriomatic.app.data.remote.api.request.LoginRequest
import com.nutriomatic.app.data.remote.api.request.RegisterRequest
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.LoginResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getProfile(
    ): ProfileResponse

    @GET("product/")
    suspend fun getProducts(
        @Query("pageSize") pageSize: Int = 5,
        @Query("page") size: Int = 1
    ): ProductsResponse

    @POST("product/")
    @Multipart
    suspend fun createProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_price") productPrice: RequestBody,
        @Part("product_desc") productDesc: RequestBody,
        @Part("product_isshow") productIsshow: RequestBody,
        @Part("product_lemaktotal") productLemakTotal: RequestBody,
        @Part("product_protein") productProtein: RequestBody,
        @Part("product_karbohidrat") productKarbohidrat: RequestBody,
        @Part("product_garam") productGaram: RequestBody,
        @Part("product_grade") productGrade: RequestBody,
        @Part("product_servingsize") productServingSize: RequestBody,
        @Part("pt_name") ptName: RequestBody,
        @Part file: MultipartBody.Part
    ): CreateProductResponse
//
//    @Multipart
//    @POST("stories")
//    suspend fun uploadStory(
//        @Part file: MultipartBody.Part,
//        @Part("description") description: RequestBody,
//        @Part("lat") lat: Double = 0.0,
//        @Part("lon") lon: Double = 0.0
//    ): FileUploadResponse
//

}