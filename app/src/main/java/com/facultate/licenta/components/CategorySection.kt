package com.facultate.licenta.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlin.math.ceil

@Composable
fun CategorySection(categoryName: String, categoryList:List<String>){
    Column(
        verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = categoryName,
            color = Variables.blue3,
            style = Typography.h4,
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            verticalArrangement = Arrangement.spacedBy(
                Variables.innerItemGap,
                Alignment.CenterVertically
            ),
            horizontalArrangement = Arrangement.spacedBy(
                Variables.innerItemGap,
                Alignment.Start
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height((ceil((categoryList.size / 3.0)) * 112).dp)
        ) {
            items(items = categoryList) { category ->
                CategoryCard(categoryName = category)
            }
        }
    }
}