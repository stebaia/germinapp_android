package com.google.maps.android.compose.ui.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldTopPanelShift(
    buttonContent: @Composable() () -> Unit,
    main: @Composable() () -> Unit,
    sidePanel: @Composable() () -> Unit
) {
    val drawerState = remember { mutableStateOf(DrawerValue.Closed) }
    Column() {

        Row() {
            Button(
                onClick = {
                    if (drawerState.value == DrawerValue.Closed) {
                        drawerState.value = DrawerValue.Open
                    } else {
                        drawerState.value = DrawerValue.Closed
                    }
                }
            ) {
                buttonContent()
            }

        }
        if (drawerState.value == DrawerValue.Open) {

            SidePanel(sidePanel)
        }
        MainContent(main)
    }
}


@Preview(widthDp = 500, heightDp = 500)
@Composable
fun DefaultPreviewTop() {
    Germinapp_newTheme {
        ScaffoldTopPanelShift(
            {
                Text("R")
            }, {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Button(onClick = { Log.d("t", "t") }) {
                        Text(text = "text")
                    }
                }
            },
            {
                Column {
                    Text(text = "side")
                }
            }
        )
    }
}