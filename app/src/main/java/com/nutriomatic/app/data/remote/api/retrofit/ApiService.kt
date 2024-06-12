package com.nutriomatic.app.data.remote.api.retrofit

import com.nutriomatic.app.data.remote.api.request.CreateStoreRequest
import com.nutriomatic.app.data.remote.api.request.LoginRequest
import com.nutriomatic.app.data.remote.api.request.RegisterRequest
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.LoginResponse
import com.nutriomatic.app.data.remote.api.response.ProductAdvertiseResponse
import com.nutriomatic.app.data.remote.api.response.ProductByIdResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.response.RegisterResponse
import com.nutriomatic.app.data.remote.api.response.StoreResponse
import com.nutriomatic.app.data.remote.api.response.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //    Auth
    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getProfile(
    ): ProfileResponse


    //    Profile
    @PATCH("user/")
    @Multipart
    suspend fun updateProfile(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("telephone") telephone: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("height") height: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("weight_goal") weightGoal: RequestBody,
        @Part("al_type") alType: RequestBody,
        @Part("hg_type") hgType: RequestBody,
        @Part file: MultipartBody.Part? = null,
    ): UpdateProfileResponse


    //    Store
    @POST("store/")
    suspend fun createStore(@Body createStoreRequest: CreateStoreRequest): BasicResponse

    @GET("store/")
    suspend fun getStore(
    ): StoreResponse


    //    Product
    @GET("product/")
    suspend fun getProducts(
        @Query("pageSize") pageSize: Int = 5,
        @Query("page") size: Int = 1,
    ): ProductsResponse

    @GET("product/advertise")
    suspend fun getProductsAdvertise(
        @Query("pageSize") pageSize: Int = 5,
        @Query("page") size: Int = 1,
    ): ProductAdvertiseResponse


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
        @Part("pt_type") ptType: RequestBody,
        @Part file: MultipartBody.Part,
    ): CreateProductResponse


    @GET("product/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
    ): ProductByIdResponse
}