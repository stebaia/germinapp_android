package com.google.maps.android.compose.ui.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import it.bsdsoftware.germinapp_new.domain.entities.Contract

@Composable
fun LiveDataMap(
    ldList: LiveData<List<Contract>>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val listRem = remember { mutableStateListOf<Contract>() }
    ldList.observe(lifecycleOwner) { list ->
        listRem.clear()
        listRem.addAll(list)
    }

        val pointerZeroCesena = LatLng(44.133331, 12.233333)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(pointerZeroCesena, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,

        ) {
            val listOfCorrectPlace = arrayListOf<Contract>()
            listRem.forEach{ it ->
                    try {
                        val pointer = LatLng(it.latitudine2.toDouble(), it.longitudine2.toDouble())
                        listOfCorrectPlace.add(it)
                    }catch (ex: Exception){

                    }
            }
            listOfCorrectPlace.forEach {
                val pointer = LatLng(it.latitudine2.toDouble(), it.longitudine2.toDouble())
                Marker(
                    state = MarkerState(position = pointer),
                    title = it.localita,
                    snippet = it.indirizzo
                )
            }

        }

}