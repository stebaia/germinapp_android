package it.bsdsoftware.germinapp_new.detail_section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.bsdsoftware.germinapp_new.domain.entities.Contract

class SelectViewModelContract : ViewModel() {


    private val _selected = MutableLiveData<Contract>()
    val selected: LiveData<Contract> = _selected

    fun select(selected: Contract) {
        _selected.postValue(selected)
    }

    class SelectViewModelContractFactory : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(SelectViewModelContract::class.java)) {
                SelectViewModelContract() as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}