package com.google.maps.android.compose.detection

import androidx.lifecycle.*
import it.bsdsoftware.germinapp_new.detail_section.SelectViewModelDetection
import it.bsdsoftware.germinapp_new.domain.entities.Contract
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetectionViewModel(
    val selectViewModelDetection: SelectViewModelDetection,
    private val useCase: DetectionUseCase
) : ViewModel() {

    private val _newDetection = MutableLiveData<Boolean>()
    val newDetection: LiveData<Boolean> = _newDetection

    private val _detections = MutableLiveData<List<Detection>>()

    val detections: LiveData<List<Detection>> = _detections

    fun newDetection() {
        _newDetection.postValue(true)
    }

    fun getAllDetections(contract: Contract) {
        viewModelScope.launch(Dispatchers.IO) {
            useCase.getPhytosanitaries({ phytosanitaries ->
                viewModelScope.launch(Dispatchers.IO) {
                    useCase.getDetections(contract, phytosanitaries, { detectionsInner ->
                        _detections.postValue(detectionsInner)
                        selectViewModelDetection.selected.value?.let { detection ->
                            selectViewModelDetection.select(detectionsInner.find { detection.localId == it.localId }!!)
                        }
                    }, {

                    })
                }
            }, {

            })
        }
    }

    fun addDetection(detection: Detection) {
        _detections.postValue(
            if (detection.new) {
                _detections.value?.plus(detection)
            } else if (detection.changed) {
                _detections.value?.filter { it.localId != detection.localId }?.plus(detection)
            } else {
                _detections.value
            }
        )
    }

    class DetectionViewModelFactory(
        private val selectViewModelDetection: SelectViewModelDetection,
        val useCase: DetectionUseCase
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DetectionViewModel::class.java)) {
                DetectionViewModel(selectViewModelDetection, useCase) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}