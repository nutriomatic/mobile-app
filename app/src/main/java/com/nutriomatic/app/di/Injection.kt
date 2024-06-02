package com.nutriomatic.app.di

import android.content.Context
import com.nutriomatic.app.data.pref.UserPreference
import com.nutriomatic.app.data.pref.dataStore
import com.nutriomatic.app.data.remote.api.retrofit.ApiConfig
import com.nutriomatic.app.data.remote.repository.UserRepository


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()

        return UserRepository.getInstance(pref, apiService)
    }
}