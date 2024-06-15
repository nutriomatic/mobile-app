package com.nutriomatic.app.presentation.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nutriomatic.app.data.fake.FakeDataSource
import com.nutriomatic.app.data.fake.model.NutritionScan

class HistoryViewModel : ViewModel() {
    private var _selected: MutableLiveData<List<Boolean>> = MutableLiveData(List(4) { true })
    var selected: LiveData<List<Boolean>> = _selected

    val scans: List<NutritionScan> = FakeDataSource.generateFakeScans()

    fun updateSelected(selected: List<Boolean>) {
        _selected.value = selected
    }
}