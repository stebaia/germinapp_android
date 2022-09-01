package com.google.maps.android.compose.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.bsdsoftware.germinapp_new.domain.entities.Contract

class SelectDetectionViewModel : ViewModel() {


    private val _detection = MutableLiveData<Contract>()
    val detection: LiveData<Contract> = _detection

    fun select(contract: Contract) {
        _detection.postValue(contract)
    }

    class DetailsViewModelFactory : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SelectDetectionViewModel::class.java)) {
                SelectDetectionViewModel() as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}