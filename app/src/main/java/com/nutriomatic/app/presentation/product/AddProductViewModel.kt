package com.nutriomatic.app.presentation.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.CreateProductResponse
import com.nutriomatic.app.data.remote.repository.ProductRepository
import kotlinx.coroutines.launch
import java.io.File

class AddProductViewModel(private val repository: ProductRepository) : ViewModel() {
    val statusCreateProduct: LiveData<Result<CreateProductResponse>> =
        repository.statusCreateProduct

    fun createProduct(
        product_name: String,
        product_price: Double = 20.0,
        product_desc: String,
        product_isshow: Boolean = false,
        product_lemaktotal: Double,
        product_protein: Double,
        product_karbohidrat: Double,
        product_garam: Double,
        product_grade: String = "Z",
        productServingsize: Int,
        pt_name: String,
        productPicture: File,
    ) {
        viewModelScope.launch {
            repository.createProduct(
                product_name,
                product_price,
                product_desc,
                product_isshow,
                product_lemaktotal,
                product_protein,
                product_karbohidrat,
                product_garam,
                product_grade,
                productServingsize,
                pt_name,
                productPicture
            )
        }
    }
}