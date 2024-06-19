package com.nutriomatic.app.presentation.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.BasicResponse
import com.nutriomatic.app.data.remote.repository.NutritionScanRepository
import com.nutriomatic.app.presentation.helper.util.createRequestBodyText
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PreviewViewModel(private val nutritionScanRepository: NutritionScanRepository) : ViewModel() {
    val createNutritionScanResponse: LiveData<Result<BasicResponse>> =
        nutritionScanRepository.createNutritionScanResponse

    fun createNutritionScan(name: String, file: MultipartBody.Part) {
        viewModelScope.launch {
            val nameBody = createRequestBodyText(name)
            nutritionScanRepository.createNutritionScan(nameBody, file)
        }
    }
}
