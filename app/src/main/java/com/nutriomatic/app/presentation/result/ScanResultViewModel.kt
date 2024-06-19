package com.nutriomatic.app.presentation.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutriomatic.app.data.remote.Result
import com.nutriomatic.app.data.remote.api.response.GetNutritionScanByIdResponse
import com.nutriomatic.app.data.remote.repository.NutritionScanRepository
import kotlinx.coroutines.launch

class ScanResultViewModel(private val nutritionScanRepository: NutritionScanRepository) :
    ViewModel() {
    val getNutritionScanByIdResponse: LiveData<Result<GetNutritionScanByIdResponse>> =
        nutritionScanRepository.getNutritionScanByIdResponse

    fun getNutritionScanById(id: String) {
        viewModelScope.launch {
            nutritionScanRepository.getNutritionScanById(id)
        }
    }
}