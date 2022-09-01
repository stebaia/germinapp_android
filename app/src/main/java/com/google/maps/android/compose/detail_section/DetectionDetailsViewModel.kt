package it.bsdsoftware.germinapp_new.detail_section

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.*
import com.google.maps.android.compose.detection.DetectionViewModel
import it.bsdsoftware.germinapp_new.domain.entities.Detection
import it.bsdsoftware.germinapp_new.domain.entities.Phytosanitary
import it.bsdsoftware.germinapp_new.domain.entities.PhytosanitaryEntity
import it.bsdsoftware.germinapp_new.domain.usecases.DetectionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class DetectionDetailsViewModel(
    val selectViewModelDetection: SelectViewModelDetection,
    val detectionViewModel: DetectionViewModel,
    private val detectionUseCase: DetectionUseCase,
    private val application: Application
) : ViewModel() {

    private val _phytosanitaries = MutableLiveData<List<PhytosanitaryEntity>>()
    val phytosanitaries: LiveData<List<PhytosanitaryEntity>> = _phytosanitaries

    fun getPhytosanitaries() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                detectionUseCase.getPhytosanitaries(
                    selectViewModelDetection.selected.value!!.contract.coltura.specie.id,
                    {
                        _phytosanitaries.postValue(it)
                    },
                    {
                        val c = 0
                    })
            }
        }

    }

    fun saveDetection(
        date: String,
        startTime: String,
        endTime: String,
        note: String,
        images: List<Triple<Bitmap, Boolean, Int>>,
        phytosanitaries: List<Phytosanitary>,
        complete: () -> Unit
    ) {
        val context = application.applicationContext
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val random = Random()
                val detection = Detection(
                    contract = selectViewModelDetection.selected.value!!.contract,
                    remoteId = selectViewModelDetection.selected.value!!.remoteId,
                    localId = selectViewModelDetection.selected.value!!.localId,
                    user = selectViewModelDetection.selected.value!!.user,
                    data = date,
                    startTime = startTime,
                    endTime = endTime,
                    images = images
                        .map {
                            Pair(
                                saveToInternalStorage(it.first, random.nextInt(), context),
                                it.second
                            )
                        }
                        .filter { it.first != null && it.second }
                        .map {
                            Pair(
                                it.first!!.path,
                                true
                            )
                        } + selectViewModelDetection.selected.value!!.images,
                    note = note,
                    phytosanitaries = phytosanitaries,
                    new = selectViewModelDetection.selected.value!!.new,
                    changed = if (selectViewModelDetection.selected.value!!.new) {
                        false
                    } else {
                        selectViewModelDetection.selected.value!!.changed
                    }
                )
                detectionUseCase.saveDetection(detection, { detectionSaved: Detection ->
                    detectionViewModel.addDetection(detectionSaved)
                    complete()
                }, {
                    val c = 0
                })
            }
        }
    }

    private fun saveToInternalStorage(bitmapImage: Bitmap, random: Int, mContext: Context): File? {
        val cw = ContextWrapper(mContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, getTime() + random + ".jpg")
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(mypath)
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return mypath
    }

    fun getFromInternalStorage(path: String): Bitmap? {
        return try {
            val imgFile = File(path)
            BitmapFactory.decodeFile(imgFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    class DetectionDetailsViewModelFactory(
        val selectViewModelDetection: SelectViewModelDetection,
        private val detectionViewModel: DetectionViewModel,
        private val detectionUseCase: DetectionUseCase,
        private val app: Application
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(DetectionDetailsViewModel::class.java)) {
                DetectionDetailsViewModel(
                    selectViewModelDetection,
                    detectionViewModel,
                    detectionUseCase,
                    app
                ) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }

}