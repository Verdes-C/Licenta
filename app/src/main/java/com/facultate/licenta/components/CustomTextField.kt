package com.facultate.licenta.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    initialValue: String = "",
    label: String,
    placeholder: String,
    textColor: Color = Variables.grey3,
    backgroundColor: Color = Color(0x338DA1DE),
    hideIndicators: Boolean = false,
    fixedOneLine: Boolean = true,
    isError: Boolean = false,
    isPassword: Boolean = false,
    onValueChange: (newValue: String) -> Unit,
) {
    TextField(
        modifier = modifier
            .heightIn(min = 56.dp, max = 80.dp)
            .fillMaxWidth(),
        value = initialValue,
        isError = isError,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        label = {
            Text(
                text = label,
                color = textColor,
                style = Typography.caption,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
        },
        placeholder = {
            Text(
                placeholder,
                color = textColor,
                modifier = Modifier.fillMaxWidth(),
                style = Typography.p,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = backgroundColor,
            unfocusedIndicatorColor = if (hideIndicators) Color.Transparent else Variables.grey6,
        ),
        shape = RoundedCornerShape(
            topStart = Variables.cornerRadius,
            topEnd = Variables.cornerRadius
        ),
        textStyle = Typography.p,
        maxLines = if (fixedOneLine) 1 else 99,
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}