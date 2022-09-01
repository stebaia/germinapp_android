package it.bsdsoftware.germinapp_new.detail_section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.bsdsoftware.germinapp_new.domain.entities.Detection

class SelectViewModelDetection : ViewModel() {


    private val _selected = MutableLiveData<Detection>()
    val selected: LiveData<Detection> = _selected

    fun select(selected: Detection) {
        _selected.postValue(selected)
    }

    class SelectViewModelDetectionFactory : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SelectViewModelDetection::class.java)) {
                SelectViewModelDetection() as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}