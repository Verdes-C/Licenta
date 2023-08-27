package com.facultate.licenta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.facultate.licenta.R
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    val searchText by remember {
        mutableStateOf("Search...")
    }
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = Variables.outerItemGap)
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color(0xFFF5F5F5),
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
    ) {
        TextAsInput(
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(align = Alignment.CenterVertically),
            placeholder = "Search ...",
            placeholderColor = Variables.blue3,
            textColor = Variables.blue3,
            textStyle = Typography.pBig.copy(fontWeight = FontWeight.Bold),
            keyboardAction = ImeAction.Search
        ) {
//!            handle search
        }
        IconButton(
            onClick = { }
        ) {
            Box(
                modifier = Modifier
                    .size(size = 32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.material_symbols_search),
                    contentDescription = "Search",
                    tint = Variables.blue3,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
