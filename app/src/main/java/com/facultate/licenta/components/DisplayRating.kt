package com.facultate.licenta.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.facultate.licenta.R

@Composable
fun DisplayRating(rating: Double, iconSize: Dp = 16.dp) {
    LazyRow(
        modifier = Modifier.height(24.dp),
        horizontalArrangement = Arrangement.Start,
    ) {
        if (rating == 5.0) {
            repeat(5) {
                item {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.icon_star_fill),
                        contentDescription = "Filled star",
                        tint = Color.Unspecified
                    )
                }
            }
        } else {
            repeat(rating.toInt()) {
                item {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.icon_star_fill),
                        contentDescription = "Filled star",
                        tint = Color.Unspecified
                    )
                }
            }
            if ((rating - rating.toInt()) >= 0.5) {
                item {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.icon_star_half),
                        contentDescription = "Half star",
                        tint = Color.Unspecified
                    )
                }
            } else {
                item {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.icon_star_empty),
                        contentDescription = "Empty star",
                        tint = Color.Unspecified
                    )
                }
            }
            repeat(5 - rating.toInt() - 1) {
                item {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(id = R.drawable.icon_star_empty),
                        contentDescription = "Empty star",
                        tint = Color.Unspecified
                    )

                }
            }
        }
    }
}