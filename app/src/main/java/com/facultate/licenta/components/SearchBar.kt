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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.facultate.licenta.R
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(modifier: Modifier = Modifier, initialValue: String = "", onSearch: (String)->Unit) {
    var searchQuery by remember{
        mutableStateOf(initialValue)
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
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
        TextField(
            maxLines = 1,
            placeholder = { Text(text = "Search ...", color = Variables.blue3, style = Typography.pBig) },
            value = searchQuery,
            onValueChange = { newValue ->
                searchQuery = newValue
            },
            textStyle = Typography.pBig,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearch(searchQuery)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Variables.blue3,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Variables.blue3
            ),
            modifier = modifier.weight(1f)
        )
        IconButton(
            onClick = {
                onSearch(searchQuery)
            }
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
