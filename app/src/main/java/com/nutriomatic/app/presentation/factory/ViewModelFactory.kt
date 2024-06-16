package com.nutriomatic.app.presentation.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nutriomatic.app.data.remote.repository.ProductRepository
import com.nutriomatic.app.data.remote.repository.StoreRepository
import com.nutriomatic.app.data.remote.repository.UserRepository
import com.nutriomatic.app.di.Injection
import com.nutriomatic.app.presentation.admin.AdminHomeViewModel
import com.nutriomatic.app.presentation.auth.AuthViewModel
import com.nutriomatic.app.presentation.details.ProductDetailViewModel
import com.nutriomatic.app.presentation.history.HistoryViewModel
import com.nutriomatic.app.presentation.home.HomeViewModel
import com.nutriomatic.app.presentation.product.AddProductViewModel
import com.nutriomatic.app.presentation.profile.ProfileViewModel
import com.nutriomatic.app.presentation.store.StoreViewModel


class ViewModelFactory(
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository,
    private val storeRepository: StoreRepository,
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(userRepository) as T
            }

            modelClass.isAssignableFrom(StoreViewModel::class.java) -> {
                StoreViewModel(productRepository, storeRepository) as T
            }

            modelClass.isAssignableFrom(AddProductViewModel::class.java) -> {
                AddProductViewModel(productRepository) as T
            }

            modelClass.isAssignableFrom(ProductDetailViewModel::class.java) -> {
                ProductDetailViewModel(productRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, productRepository) as T
            }

            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel() as T
            }

            modelClass.isAssignableFrom(AdminHomeViewModel::class.java) -> {
                AdminHomeViewModel(userRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.provideRepository(context),
                        Injection.provideProductRepository(context),
                        Injection.provideStoreRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}