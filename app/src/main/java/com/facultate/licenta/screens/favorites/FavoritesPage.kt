package com.facultate.licenta.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.DisplayFavoritesItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesPage(
    navController: NavHostController,
    viewModel: FavoritesViewmodel = hiltViewModel(),
) {

    val favoritesItems by remember {
        mutableStateOf(
            listOf(
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem(),
                FavoritesItem()
            )
        )
    }

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.Favorites,
                navController = navController
            ) {}
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Variables.grey1)
                .padding(paddingValues = paddingValues)
                .padding(
                    horizontal = Variables.outerItemGap
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            if (favoritesItems.isEmpty()) {
                item {
                    DisplayEmptyFavorites()
                }
            } else {
                items(items = favoritesItems) { favoriteItem ->
                    DisplayFavoritesItem(modifier = Modifier, favoritesItem = favoriteItem)
                }
            }
        }
    }
}

@Composable
fun DisplayEmptyFavorites() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "It is quite empty in here.\nLet’s go searching",
            style = Typography.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = Variables.innerItemGap)
                .fillMaxWidth()
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_broken_heart),
            contentDescription = "Empty favorites icon",
            tint = Color.Unspecified,
            modifier = Modifier.padding(bottom = Variables.innerItemGap)
        )
    }
}

data class FavoritesItem(
//    TODO remove defaults other than quantity
    val productId: String = "123123123",
    val productName: String = "ProductName",
    val productImage: Int = R.drawable.image_placeholder,
    val productImageDescription: String = "Product image description",
    val productPrice: Double = 123.24,
    val reviewsNumber: Int = 123,
    val rating: Double = 3.6,
)
