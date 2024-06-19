package com.nutriomatic.app.data.remote.api.retrofit

import com.nutriomatic.app.data.remote.api.request.CreateTransactionRequest
import com.nutriomatic.app.data.remote.api.request.LoginRequest
import com.nutriomatic.app.data.remote.api.request.RegisterRequest
import com.nutriomatic.app.data.remote.api.request.StoreRequest
import com.nutriomatic.app.data.remote.api.request.UpdateStoreRequest
import com.nutriomatic.app.data.remote.api.request.UpdateTransactionRequest
import com.nutriomatic.app.data.remote.api.response.AllTransactionsResponse
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.api.response.ClassificationCaloryResponse
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.api.response.GetNutritionScanByIdResponse
import com.nutriomatic.app.data.remote.api.response.GetNutritionScanByUserIdResponse
import com.nutriomatic.app.data.remote.api.response.GetTransactionByIdResponse
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
import retrofit2.http.DELETE
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
        @Part("telp") telephone: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("height") height: RequestBody,
        @Part("weight") weight: RequestBody,
        @Part("weight_goal") weightGoal: RequestBody,
        @Part("al_type") alType: RequestBody,
        @Part("hg_type") hgType: RequestBody,
        @Part file: MultipartBody.Part? = null,
    ): UpdateProfileResponse

    // user classification and calory
    @GET("user/cal")
    suspend fun getClassification(
    ): ClassificationCaloryResponse


    //    Store
    @POST("store/")
    suspend fun createStore(@Body createStoreRequest: StoreRequest): BasicResponse

    @GET("store/")
    suspend fun getStore(
    ): StoreResponse

    @PATCH("store/")
    suspend fun updateStore(
        @Body updateStoreRequest: UpdateStoreRequest,
    ): BasicResponse


    //    Product
    @GET("product/")
    suspend fun getProducts(
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
    ): ProductsResponse

    @GET("product/store/{store_id}")
    suspend fun getProductsStore(
        @Path("store_id") id: String,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
    ): ProductsResponse

    @GET("product/advertise")
    suspend fun getProductsAdvertise(
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
    ): ProductAdvertiseResponse

    @POST("product/advertise/{id}")
    suspend fun advertiseProduct(
        @Path("id") id: String,
    ): BasicResponse


    @POST("product/")
    @Multipart
    suspend fun createProduct(
        @Part("product_name") productName: RequestBody,
        @Part("product_price") productPrice: RequestBody,
        @Part("product_desc") productDesc: RequestBody,
        @Part("product_isshow") productIsshow: RequestBody,
        @Part("product_energi") productEnergi: RequestBody,
        @Part("product_gula") productGula: RequestBody,
        @Part("product_lemaktotal") productLemakTotal: RequestBody,
        @Part("product_protein") productProtein: RequestBody,
        @Part("product_karbohidrat") productKarbohidrat: RequestBody,
        @Part("product_garam") productGaram: RequestBody,
        @Part("product_grade") productGrade: RequestBody,
        @Part("product_servingsize") productServingSize: RequestBody,
        @Part("pt_type") ptType: RequestBody,
        @Part file: MultipartBody.Part,
    ): CreateProductResponse


    @PATCH("product/{id}")
    @Multipart
    suspend fun updateProductById(
        @Path("id") id: String,
        @Part("product_name") productName: RequestBody,
        @Part("product_price") productPrice: RequestBody,
        @Part("product_desc") productDesc: RequestBody,
        @Part("product_isshow") productIsshow: RequestBody,
        @Part("product_energi") productEnergi: RequestBody,
        @Part("product_gula") productGula: RequestBody,
        @Part("product_lemaktotal") productLemakTotal: RequestBody,
        @Part("product_protein") productProtein: RequestBody,
        @Part("product_karbohidrat") productKarbohidrat: RequestBody,
        @Part("product_garam") productGaram: RequestBody,
        @Part("product_grade") productGrade: RequestBody,
        @Part("product_servingsize") productServingSize: RequestBody,
        @Part("pt_type") ptType: RequestBody,
        @Part file: MultipartBody.Part?,
    ): BasicResponse

    @PATCH("product/{id}")
    @Multipart
    suspend fun updateProductIsShow(
        @Path("id") id: String,
        @Part("product_isshow") productIsshow: RequestBody,
    ): BasicResponse


    @GET("product/{id}")
    suspend fun getProductById(
        @Path("id") id: String,
    ): ProductByIdResponse

    @GET("product/advertise")
    suspend fun searchProductAdvertiseByName(
        @Query("search") search: String,
    ): ProductAdvertiseResponse

    @DELETE("product/{id}")
    suspend fun deleteProductById(
        @Path("id") id: String,
    ): BasicResponse


    // transaction

    @GET("transaction/")
    suspend fun getAllTransactions(
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
    ): AllTransactionsResponse

    @GET("transaction/{id}")
    suspend fun getTransactionById(
        @Path("id") id: String,
    ): GetTransactionByIdResponse

    @POST("transaction/{product_id}")
    suspend fun createTransaction(
        @Path("product_id") id: String,
        @Body createTransactionRequest: CreateTransactionRequest,
    ): BasicResponse

    @POST("transaction/proof/{id}")
    suspend fun uploadProofTransaction(
        @Path("id") id: String,
        @Part photo: MultipartBody.Part,
    ): BasicResponse

    @PATCH("transaction/status/{id}")
    suspend fun updateTransactionStatus(
        @Path("id") id: String,
        @Body updateTransactionRequest: UpdateTransactionRequest,
    ): BasicResponse



    // nutrition scan
    @POST("scanned-nutrition/")
    @Multipart
    suspend fun createNutritionScan(
        @Part("sn_name") name: RequestBody,
        @Part photo: MultipartBody.Part,
    ): BasicResponse

    @GET("scanned-nutrition/{id}")
    suspend fun getNutritionScanById(
        @Path("id") id: String,
    ): GetNutritionScanByIdResponse

    @GET("scanned-nutrition/")
    suspend fun getNutritionScanByUserId(
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
    ): GetNutritionScanByUserIdResponse
}