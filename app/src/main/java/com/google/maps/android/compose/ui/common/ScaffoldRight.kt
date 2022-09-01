package com.google.maps.android.compose.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme
import kotlinx.coroutines.launch


@Composable
fun customShape(screenWidth: Int): Shape {
    return object : Shape {
        override fun createOutline(
            size: Size,
            layoutDirection: LayoutDirection,
            density: Density
        ): Outline {

            return Outline.Rectangle(
                Rect(
                    left = if (screenWidth > 400) if (screenWidth < 1000) size.width * 1 / 2 else size.width * 2 / 3 else size.width / 100,
                    top = 0f,
                    right = size.width,
                    bottom = size.height
                )
            )
        }
    }
}

@Composable
fun ScaffoldRight(
    main: @Composable () -> Unit,
    bar: @Composable () -> Unit,
    sidePanel: @Composable () -> Unit,
    sidePanelBar: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(
        DrawerValue.Closed
    )
    val scope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        ModalDrawer(
            drawerShape = customShape(screenWidth),
            drawerState = drawerState,
            drawerContent = {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    Row(
                        modifier = Modifier.fillMaxWidth(if (screenWidth > 400) if (screenWidth < 1000) 1f / 2 else 1f / 3 else 90f),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),) {
                            sidePanelBar()
                        }
                        IconButton(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            onClick = {
                                scope.launch {
                                    drawerState.close()
                                }
                            }) {
                            Icon(
                                Icons.Default.Close,
                                "Close",
                                tint = Color.Gray
                            )
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(if (screenWidth > 400) if (screenWidth < 1000) 1f / 2 else 1f / 3 else 90f)
                            .fillMaxHeight()
                            .padding(horizontal = 10.dp)
                    ) {
                        sidePanel()
                    }
                }
            },
            content = {
                Column(
                    Modifier.fillMaxWidth(),
                ) {
                    TopAppBar(
                        backgroundColor = Color.White
                    )
                    {
                        IconButton(
                            modifier = Modifier.padding(horizontal = 0.dp),
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                            Icon(
                                Icons.Default.Menu,
                                "Menu",
                                tint = Color.Red
                            )
                        }
                        bar()
                    }
                    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                        MainContent(main)
                    }
                }
            }
        )
    }
}

@Preview(widthDp = 400, heightDp = 500)
@Composable
fun DefaultPreviewScaffoldRight() {
    Germinapp_newTheme {
        ScaffoldRight(
            {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = { }) {
                        Text(text = "text")
                    }
                }
            },
            {
                Text(
                    "Title",
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                )
                Button(
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    onClick = {

                    }
                ) {
                    Text(
                        text = "Filter"
                    )
                }
            },
            {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = "side")
                }
            },
            {
                Text(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    text = "test"
                )
            }
        )
    }
}