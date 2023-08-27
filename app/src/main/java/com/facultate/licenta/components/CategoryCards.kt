package com.facultate.licenta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables


@Composable
fun CategoryCard(modifier: Modifier = Modifier, categoryName: String = "Category") {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .requiredSize(size = 100.dp)
            .shadow(
                elevation = 4.dp,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor,
                shape = RoundedCornerShape(Variables.cornerRadius)
                )
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(horizontal = 12.dp)

    ) {
        Text(
            text = categoryName,
            color = Variables.blue2,
            textAlign = TextAlign.Center,
            style = Typography.p,
            fontWeight = FontWeight(600),
            modifier = Modifier
                .fillMaxSize()
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}