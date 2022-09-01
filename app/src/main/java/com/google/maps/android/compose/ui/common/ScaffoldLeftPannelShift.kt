package com.google.maps.android.compose.ui.common

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.DrawerValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldLeftPanelShift(
    phone: Float,
    tablet: Float,
    buttonContent: @Composable (() -> Unit) -> Unit,
    main: @Composable () -> Unit,
    sidePanel: @Composable () -> Unit
) {
    val drawerState = remember { mutableStateOf(DrawerValue.Closed) }
    BoxWithConstraints {
        val maxWidth = this.maxWidth
        val scale = if (maxWidth < 500.dp) phone else tablet
        if (drawerState.value == DrawerValue.Open) {
            Row() {
                Box(modifier = Modifier.fillMaxWidth(1 - scale)) {
                    SidePanel(sidePanel)
                }
                Box(modifier = Modifier.requiredWidth(maxWidth)) {
                    Row(modifier = Modifier.requiredWidth(maxWidth)) {
                        buttonContent{
                            if (drawerState.value == DrawerValue.Closed) {
                                drawerState.value = DrawerValue.Open
                            } else {
                                drawerState.value = DrawerValue.Closed
                            }
                        }
                        MainContent(main)
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row() {
                    buttonContent {
                        if (drawerState.value == DrawerValue.Closed) {
                            drawerState.value = DrawerValue.Open
                        } else {
                            drawerState.value = DrawerValue.Closed
                        }
                    }
                    MainContent(main)
                }
            }
        }
    }
}

@Preview(widthDp = 500, heightDp = 500)
@Composable
fun DefaultPreviewScaffold() {
    Germinapp_newTheme {
        ScaffoldLeftPanelShift( 0.5f, 0.7f,
            {
                Button(
                    onClick = {
                        it()
                    }
                ) {
                    Text("R")
                }
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