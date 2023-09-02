package com.facultate.licenta.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    displayArrow: Boolean = false,
    menuEntry: MenuEntry,
    navController: NavHostController? = null,
    navigate: () -> Unit = {
//    do not navigate
    },
) {
    val colorAfterLogoutCheck =
        if (menuEntry != MenuEntries.Logout) Variables.blue3 else Variables.red

    Row(
        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.grey6,
                ambientColor = Variables.grey6,
                shape = RoundedCornerShape(size = Variables.cornerRadius)
            )
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Variables.topBarBackground)
            .padding(all = 8.dp)
            .clickable(
                //_ set the onClick animation to null
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                navigate.invoke()
            }
    ) {
        if (displayArrow) {
            Icon(
                painter = painterResource(id = R.drawable.icon_arrow_left),
                contentDescription = "Arrow - go back",
                tint = Variables.blue3,
                modifier = Modifier
                    .requiredSize(size = 30.dp)
                    .clickable(
                        //_ set the onClick animation to null
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { navController?.popBackStack() }
            )
        }

        if (menuEntry.icon != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .requiredSize(size = 40.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color(0xFFDDE1F1))
            ) {
                Icon(
                    painter = painterResource(id = menuEntry.icon),
                    contentDescription = menuEntry.iconDescription,
                    tint = colorAfterLogoutCheck,
                    modifier = Modifier
                        .requiredSize(size = 24.dp)
                )
            }
        }
        Text(
            text = menuEntry.name,
            color = colorAfterLogoutCheck,
            style = Typography.h4,
            fontWeight = FontWeight(700),
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(vertical = 4.dp),
            maxLines = 2
        )
    }
}


object MenuEntries {
    val Orders =
        MenuEntry(name = "Orders", icon = R.drawable.icon_order, iconDescription = "Orders icon")
    val Vouchers = MenuEntry(
        name = "Vouchers",
        icon = R.drawable.icon_coupon,
        iconDescription = "Vouchers icon"
    )
    val Support = MenuEntry(
        name = "Support (Future)",
        icon = R.drawable.icon_support,
        iconDescription = "Support icon"
    )
    val ShippingAdress = MenuEntry(
        name = "Shipping Adress (Future)",
        icon = R.drawable.icon_shipping_box,
        iconDescription = "Shipping Adress icon"
    )
    val AccountData = MenuEntry(
        name = "Account Data",
        icon = R.drawable.icon_profile,
        iconDescription = "Account Data icon"
    )
    val Favorites = MenuEntry(
        name = "Favorites",
        icon = R.drawable.icon_favorites,
        iconDescription = "Favorites icon"
    )
    val Cart = MenuEntry(name = "Cart", icon = R.drawable.icon_cart, iconDescription = "Cart icon")
    val Logout =
        MenuEntry(name = "Logout", icon = R.drawable.icon_logout, iconDescription = "Logout icon")
}

data class MenuEntry(
    val name: String,
    val icon: Int? = null,
    val iconDescription: String,
)

//
//@Preview(widthDp = 325, showBackground = true)
//@Composable
//private fun TypeOrdersdisplayBackArrowTruePreview() {
//    Column(
//        verticalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        TopBar(Modifier, menuEntry = MenuEntries.Orders, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.Vouchers, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.Support, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.ShippingAdress, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.AccountData, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.Wishlist, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.Cart, displayArrow = true)
//        TopBar(Modifier, menuEntry = MenuEntries.Logout, displayArrow = false)
//    }
//}