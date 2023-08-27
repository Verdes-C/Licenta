package com.facultate.licenta.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.facultate.licenta.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextAsInput(
    modifier: Modifier = Modifier,
    placeholder: String,
    placeholderColor: Color = Color.Black,
    textStyle: TextStyle = Typography.p,
    textColor: Color = Color.Black,
    keyboardAction: ImeAction,
    handleAction: () -> Unit,
) {
    var textValue by remember { mutableStateOf("") }

    TextField(
        maxLines = 1,
        placeholder = { Text(text = placeholder, color = placeholderColor, style = textStyle) },
        value = textValue,
        onValueChange = { newValue ->
            textValue = newValue
        },
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = keyboardAction,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                handleAction.invoke()
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.Transparent,
            textColor = textColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = textColor
        ),
        modifier = modifier.fillMaxWidth()
    )
}