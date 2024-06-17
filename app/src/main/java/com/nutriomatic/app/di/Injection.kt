package com.nutriomatic.app.di

import android.content.Context
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.pref.dataStore
import com.nutriomatic.app.data.remote.api.retrofit.ApiConfig
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import com.nutriomatic.app.data.remote.repository.TransactionRepository
import com.nutriomatic.app.data.remote.repository.UserRepository


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)

        return UserRepository.getInstance(pref, apiService)
    }

    fun provideProductRepository(context: Context): ProductRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)

        return ProductRepository.getInstance(pref, apiService)
    }

    fun provideStoreRepository(context: Context): StoreRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)

        return StoreRepository.getInstance(apiService)
    }

    fun provideTransactionRepository(context: Context): TransactionRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)

        return TransactionRepository.getInstance(pref, apiService)
    }
}