package com.facultate.licenta.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.facultate.licenta.screens.product.Review
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun DisplayReview(review: Review){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(Variables.cornerRadius)
            )
            .padding(all = Variables.innerItemGapLow),
        verticalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow)
    ) {
        var linesToShow by remember {
            mutableIntStateOf(4)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = review.title,
                style = Typography.p,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = review.date,
                style = Typography.p.copy(color = Variables.grey3),
                maxLines = 1,
                modifier = Modifier
            )

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DisplayRating(rating = review.rating, iconSize = 20.dp)
        }

        Text(
            text = review.reviewBody,
            style = Typography.p.copy(color = Variables.grey3),
            maxLines = linesToShow,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.clickable {
                when(linesToShow){
                    4 -> linesToShow = 99
                    99 -> linesToShow = 4
                }
            }
        )
    }
}