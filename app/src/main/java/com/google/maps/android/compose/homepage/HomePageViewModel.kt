package com.google.maps.android.compose.homepage

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import it.bsdsoftware.germinapp_new.detail_section.DetectionDetailsViewModel
import it.bsdsoftware.germinapp_new.detail_section.DetectionNewViewModel
import it.bsdsoftware.germinapp_new.detail_section.SelectViewModelContract
import com.google.maps.android.compose.detection.DetectionViewModel
import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import it.bsdsoftware.germinapp_new.domain.usecases.LoadContractUseCase
import it.bsdsoftware.germinapp_new.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomePageViewModel(
    private val loginViewModel: LoginViewModel,
    val selectViewModelContract: SelectViewModelContract,
    val detectionViewModel: DetectionViewModel,
    val detectionNewViewModel: DetectionNewViewModel,
    val detectionDetails: DetectionDetailsViewModel,
    private val contractUseCase: LoadContractUseCase,
    private val detectionUseCase: DetectionUseCase,
) : ViewModel() {


    private val _contracts = MutableLiveData<Pair<List<Contract>, HashMap<Filters, String>>>()
    private val filters = HashMap<Filters, String>(Filters.values().size)

    init {
        for (filter in Filters.values()) {
            filters[filter] = ""
        }
    }

    fun getValueOfFilter(filter: Filters): String {
        return filters[filter]!!
    }

    val contracts: LiveData<List<Contract>> = Transformations.map(_contracts) { list ->
        list.first
            .filter {
                if (filters[Filters.ARTICOLO] != "") {
                    it.coltura.articolo.value.startsWith(filters[Filters.ARTICOLO]!!) ||
                            it.coltura.articolo.id.startsWith(filters[Filters.ARTICOLO]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.ANNO] != "") {
                    it.coltura.sigla.value.startsWith(filters[Filters.ANNO]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.COLTIVATORE] != "") {
                    it.coltivatore.value.startsWith(filters[Filters.COLTIVATORE]!!) ||
                            it.coltivatore.id.startsWith(filters[Filters.COLTIVATORE]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.COLTURA] != "") {
                    it.prodotto.coltura.startsWith(filters[Filters.COLTURA]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.DESTINAZIONE] != "") {
                    it.prodotto.destinazione.startsWith(filters[Filters.DESTINAZIONE]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.LOTTO] != "") {
                    it.coltura.lotto.startsWith(filters[Filters.LOTTO]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.SPECIE] != "") {
                    it.coltura.specie.value.startsWith(filters[Filters.SPECIE]!!) ||
                            it.coltura.specie.id.startsWith(filters[Filters.SPECIE]!!)
                } else {
                    true
                }
            }
            .filter {
                if (filters[Filters.VARIETA] != "") {
                    it.coltura.varieta.value.startsWith(filters[Filters.VARIETA]!!) ||
                            it.coltura.varieta.id.startsWith(filters[Filters.VARIETA]!!)
                } else {
                    true
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadContract() {
        loginViewModel.loggedUser.value?.get()?.token?.let { it ->
            viewModelScope.launch(Dispatchers.IO) {
                contractUseCase(it, { contracts ->
                    _contracts.postValue(Pair(contracts, filters))
                }, {
                    loginViewModel.logout()
                })
            }
        }
    }

    fun syncronize(end: () -> Unit) {
        loginViewModel.loggedUser.value?.get()?.token?.let { it ->
            viewModelScope.launch(Dispatchers.IO) {
                contractUseCase.remote(it, { contracts ->
                    _contracts.postValue(Pair(contracts, filters))
                    viewModelScope.launch(Dispatchers.IO) {
                        detectionUseCase.synchronizeDetection(it, {
                            selectViewModelContract.selected.value?.let {
                                detectionViewModel.getAllDetections(it)
                            }
                            end()
                        }) {

                        }
                    }
                }, {
                    loginViewModel.logout()
                })
            }
        }
    }

    fun filterReset() {
        for (filter in Filters.values()) {
            filters[filter] = ""
        }
        _contracts.postValue(Pair(_contracts.value!!.first, filters))
    }

    fun addFilter(filter: Filters, value: String) {
        filters[filter] = value
        _contracts.postValue(Pair(_contracts.value!!.first, filters))
    }


    class HomePageViewModelFactory(
        private val loginViewModel: LoginViewModel,
        private val detailsViewModel: SelectViewModelContract,
        private val detectionViewModel: DetectionViewModel,
        private val detectionNewViewModel: DetectionNewViewModel,
        private val detectionDetails: DetectionDetailsViewModel,
        private val contractUseCase: LoadContractUseCase,
        private val detectionUseCase: DetectionUseCase,
    ) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(HomePageViewModel::class.java)) {
                HomePageViewModel(
                    loginViewModel,
                    detailsViewModel,
                    detectionViewModel,
                    detectionNewViewModel,
                    detectionDetails,
                    contractUseCase,
                    detectionUseCase
                ) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}
