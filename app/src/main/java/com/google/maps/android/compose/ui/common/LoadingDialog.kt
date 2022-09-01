package com.google.maps.android.compose.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(openDialogCustom: MutableState<Boolean>) {
    Dialog(onDismissRequest = { openDialogCustom.value = false }) {
        CustomDialogUI(openDialogCustom = openDialogCustom)
    }
}

//Layout
@Composable
fun CustomDialogUI(modifier: Modifier = Modifier, openDialogCustom: MutableState<Boolean>) {
    Card(
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            Modifier
                .background(Color.White, RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(modifier = Modifier.background(Color.White), color = Color.Red)
            }
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .background(Color.Red),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                TextButton(onClick = {
                    openDialogCustom.value = false
                }) {
                    Text(
                        "Interrompi",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = Center,
                        modifier = modifier
                            .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Preview(name = "Custom Dialog")
@Composable
fun MyDialogUIPreview() {
    val test = remember {
        mutableStateOf(true)
    }
    Button(onClick = { test.value = true }) {
        Text(text = "text")
    }
    if (test.value) {
        CustomDialog(openDialogCustom = test)
    }
}