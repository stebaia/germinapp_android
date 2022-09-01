package com.google.maps.android.compose.ui.common

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme



@Composable
fun <T> LiveDataList(
    ldList: LiveData<List<T>>,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    element: @Composable (T)-> Unit
) {
    val listRem = remember { mutableStateListOf<T>() }
    ldList.observe(lifecycleOwner) { list ->
        listRem.clear()
        listRem.addAll(list)
    }
    LazyColumn {
        items(listRem) { elem ->
            element(elem)
        }
    }

}

@Preview(widthDp = 400, heightDp = 500)
@Composable
fun DefaultPreviewContractsList() {
    Germinapp_newTheme {
        val list = listOf<String>()
        val tmp = MutableLiveData<List<String>>()
        tmp.value = list
        LiveDataList(tmp){
            Text(text = it)
        }
        tmp.postValue(listOf("r", "rr"))
    }
}
