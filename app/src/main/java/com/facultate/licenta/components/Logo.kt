package com.facultate.licenta.components

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.facultate.licenta.R
import com.facultate.licenta.ui.theme.Variables

@Composable
fun Logo(){
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.icon_logo),
        contentDescription = "Ink Quill logo",
        modifier = Modifier,
        tint = Variables.grey6
    )
}