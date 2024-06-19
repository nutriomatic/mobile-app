package com.nutriomatic.app.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.nutriomatic.app.data.remote.api.response.NutritionScan
import com.nutriomatic.app.data.remote.repository.NutritionScanRepository

class HistoryViewModel(
    private val nutritionScanRepository: NutritionScanRepository,
) : ViewModel() {
    private var _selected: MutableLiveData<List<Boolean>> = MutableLiveData(List(4) { true })
    var selected: LiveData<List<Boolean>> = _selected

    fun updateSelected(selected: List<Boolean>) {
        _selected.value = selected
    }

    fun getNutritionScanPaging(): LiveData<PagingData<NutritionScan>> {
        return nutritionScanRepository.getNutritionScanByUserIdPaging()
    }
}