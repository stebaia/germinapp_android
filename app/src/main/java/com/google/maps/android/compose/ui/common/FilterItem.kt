package com.google.maps.android.compose.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import it.bsdsoftware.germinapp_new.ui.theme.Germinapp_newTheme


@Composable
fun TextFilterItem(
    label: String,
    textLabel: String = "",
    placeholder: String = "",
    localFocusManager: FocusManager,
    onValueChange: (String) -> Unit
) {
    Row() {
        Column() {
            Text(label)
            var value by remember { mutableStateOf("") }
            OutlinedTextField(
                value = value,
                onValueChange = {
                    value = it
                    onValueChange(value)
                },
                placeholder = { TextFieldValue(placeholder) },
                label = { Text(text = textLabel) },
                modifier = Modifier.fillMaxWidth(0.7f),
                singleLine = true,
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                }),
                trailingIcon = {
                    IconButton(onClick = {
                        value = ""
                        onValueChange(value)
                    }) {
                        Icon(imageVector = Icons.Default.Clear, "Clear")
                    }
                },
            )
        }
    }
}

@Preview(widthDp = 400, heightDp = 500)
@Composable
fun DefaultPreviewFilterItem() {
    Germinapp_newTheme {
        val localFocusManager = LocalFocusManager.current
        var value by remember { mutableStateOf("") }
        Column() {
            TextFilterItem("test", localFocusManager = localFocusManager) {
                value = it
            }
            Text(text = value)
        }
    }
}