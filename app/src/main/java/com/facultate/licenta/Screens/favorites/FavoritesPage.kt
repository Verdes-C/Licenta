package com.facultate.licenta.Screens.favorites

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.Screens.cart.CartItem
import com.facultate.licenta.components.DisplayFavoritesItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesPage(navController: NavHostController) {

    val favoritesItems by remember {
        mutableStateOf(
            listOf(
                FavoritesItem(rating = 0.2),
                FavoritesItem(rating = 0.7),
                FavoritesItem(rating = 1.2),
                FavoritesItem(rating = 1.7),
                FavoritesItem(rating = 2.2),
                FavoritesItem(rating = 2.7),
                FavoritesItem(rating = 3.2),
                FavoritesItem(rating = 3.6),
                FavoritesItem(rating = 4.2),
                FavoritesItem(rating = 4.7),
                FavoritesItem(rating = 5.0),
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
            )
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
            items(items = favoritesItems) { favoriteItem ->
                DisplayFavoritesItem(modifier = Modifier, favoritesItem = favoriteItem)
            }
        }
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
