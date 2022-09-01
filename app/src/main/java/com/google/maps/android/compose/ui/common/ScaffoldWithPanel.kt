package com.google.maps.android.compose.ui.common

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme


@Composable
fun MainContent(main: @Composable() () -> Unit) {
    return Column {
        main()
    }

}

@Composable
fun SidePanel(sidePanel: @Composable() () -> Unit) {
    return Column {
        sidePanel()
    }
}

@Composable
fun ScaffoldWithPanel(
    main: @Composable() () -> Unit,
    sidePanel: @Composable() () -> Unit
) {
    val drawerState = remember { mutableStateOf(DrawerValue.Open) }

    Scaffold(
        topBar = {
            TopAppBar()
            {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text("Title")
                    Button(
                        onClick = {
                            if (drawerState.value == DrawerValue.Closed) {
                                drawerState.value = DrawerValue.Open
                            } else {
                                drawerState.value = DrawerValue.Closed
                            }
                        }
                    ) {
                        Text(
                            text = "Filter"
                        )
                    }
                }
            }
        },
        bottomBar = {},
        content = { padding ->
            BoxWithConstraints(

                modifier = Modifier.fillMaxSize().padding()
            ) {
                val parentWidth = constraints.maxWidth.dp / 2
                val parentHeight = constraints.maxHeight.dp / 2
                val divide = if (parentWidth > 400.dp) 4 else 2
                val mainContentModifier = if (drawerState.value == DrawerValue.Open) {
                    Modifier
                        .offset(x = -(parentWidth / divide))
                        .clickable {
                            drawerState.value = DrawerValue.Closed
                        }
                } else {
                    Modifier
                }
                Box(
                    modifier = mainContentModifier.fillMaxHeight()
                ) {
                    MainContent(main)
                }
                if (drawerState.value == DrawerValue.Open) {
                    Box(
                        modifier = Modifier
                            .size(parentWidth / divide, height = parentHeight)
                            .offset(x = parentWidth * (divide - divide / 2) / divide)
                    ) {
                        SidePanel(sidePanel)
                    }
                }
            }
        }
    )
}

@Preview(widthDp = 1000, heightDp = 500)
@Composable
fun DefaultPreview() {
    Germinapp_newTheme {
        ScaffoldWithPanel(
            {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { Log.d("t","t") }) {
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