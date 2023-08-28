package com.facultate.licenta.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.facultate.licenta.screens.favorites.FavoritesItem
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun DisplayFavoritesOptionMenu(
    modifier: Modifier,
    favoritesItem: FavoritesItem,
    closeTheMenu: ()-> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.End,
        modifier = modifier
            .padding(end = 20.dp, top = 40.dp)
            .background(color = Color.White)
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .border(
                border = BorderStroke(1.dp, Variables.blue3),
                shape = RoundedCornerShape(
                    Variables.cornerRadius
                )
            )
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = "Remove from favorites",
            color = Variables.blue3,
            style = Typography.p,
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 4.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable(
                    //_ set the onClick animation to null
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    // add to favorites
                    closeTheMenu.invoke()
                })

    }
}
