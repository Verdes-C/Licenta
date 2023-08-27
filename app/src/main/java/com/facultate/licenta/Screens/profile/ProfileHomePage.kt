package com.facultate.licenta.Screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun ProfileHomePage(navController: NavHostController) {

    val entriesAndNavigation =
        listOf(
            MenuEntries.Orders to {},
            MenuEntries.Vouchers to {},
            MenuEntries.Favorites to { navController.navigate(Screens.Favorites.route) },
            MenuEntries.AccountData to {},
            MenuEntries.ShippingAdress to {},
            MenuEntries.Support to {},
            MenuEntries.Logout to {},
        )

    LazyColumn(
        modifier = Modifier
            .background(color = Variables.grey1)
            .fillMaxSize()
            .padding(
                horizontal = Variables.outerItemGap
            ),
        verticalArrangement = Arrangement.spacedBy(Variables.outerItemGap),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            ProfileUserHeading(modifier = Modifier.padding(top = Variables.outerItemGap))
        }

        //_ Profile cards
        items(items = entriesAndNavigation) { menu ->
            TopBar(menuEntry = menu.first) {
                menu.second.invoke()
            }
        }

        item {
            Text(
                text = "InkQwill is giving their regards to our dear customers! ❤️",
                textAlign = TextAlign.Center,
                style = Typography.h2,
                color = Variables.blue3
            )
        }

    }
}

@Composable
fun ProfileUserHeading(modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor
            )
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(horizontal = Variables.innerItemGap)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_placeholder),
            contentDescription = "User Profile Image",
            modifier = Modifier
                .requiredSize(size = 60.dp)
                .clip(shape = CircleShape)
        )

        Text(
            text = "Marvin McKinney",
            color = Color(0xff163688),
            style = Typography.h4,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_edit),
            contentDescription = "Edit name",
            tint = Variables.blue3
        )
    }
}
