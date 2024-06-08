package com.nutriomatic.app.data.remote.api.retrofit

import com.nutriomatic.app.data.remote.api.request.LoginRequest
import com.nutriomatic.app.data.remote.api.request.RegisterRequest
import com.nutriomatic.app.data.remote.api.response.LoginResponse
import com.nutriomatic.app.data.remote.api.response.ProductsResponse
import com.nutriomatic.app.data.remote.api.response.ProfileResponse
import com.nutriomatic.app.data.remote.api.response.RegisterResponse
import com.nutriomatic.app.data.remote.api.response.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @POST("auth/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @GET("product/?{pageSize}&{page}")
    suspend fun getProducts(
        @Header("Authorization") token: String,
        @Path("pageSize") pageSize: Int = 1,
        @Path("pageSize") page: Int = 5,
    ): ProductsResponse


}