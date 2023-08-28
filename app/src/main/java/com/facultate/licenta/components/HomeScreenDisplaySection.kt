package com.facultate.licenta.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables


@Composable
fun HomeScreenDisplaySection(
    modifier: Modifier = Modifier,
    itemGap: Dp = Variables.innerItemGap,
    displayText: String,
    textStyle: TextStyle = Typography.h3,
    showMoreOnClick: () -> Unit,
    productsToDisplay: LazyListScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = displayText,
                style = textStyle,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .weight(1f)
            )
            Text(
                text = "Show more",
                color = Variables.black,
                style = Typography.caption,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .clickable {
                        showMoreOnClick.invoke()
                    }
            )
        }
        LazyRow(
            modifier = Modifier
                .align(alignment = Alignment.Start),
            contentPadding = PaddingValues(all = Variables.innerItemGap),
            horizontalArrangement = Arrangement.spacedBy(itemGap)
        ) {
            productsToDisplay()
        }
    }
}