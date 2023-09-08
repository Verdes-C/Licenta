package com.facultate.licenta.screens.favorites

import android.net.Uri
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.DisplayFavoriteItem
import com.facultate.licenta.components.DisplayLoading
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.Product
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesPage(
    navController: NavHostController,
    viewModel: FavoritesViewmodel = hiltViewModel(),
) {

    val favoriteItems by viewModel.favoriteItems.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getFavoriteItems()
    }

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.Favorites,
                navController = navController,
            )
            {}
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
            if (favoriteItems is DataState.Loading) {
                item {
                    DisplayLoading()
                }
            } else if (favoriteItems is DataState.Error) {
                val error = (favoriteItems as DataState.Error).exception
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "${error.message}", style = Typography.pBig)
                    }
                }
            } else if (favoriteItems is DataState.Success) {
                val favorites = (favoriteItems as DataState.Success<List<Product>>).data
                if (favorites.isEmpty()) {
                    item {
                        DisplayEmptyFavorites()
                    }
                } else {
                    items(items = favorites) { favoriteItem ->
                        DisplayFavoriteItem(
                            modifier = Modifier,
                            favoritesItem = favoriteItem,
                            addToCart = {
                                viewModel.addToCart(
                                    productId = favoriteItem.id,
                                    productCategory = favoriteItem.category,
                                    discount = favoriteItem.discount,
                                    quantity = 1
                                )
                            },
                            removeFromFavorite = {
                                viewModel.removeFromFavorite(
                                    productId = favoriteItem.id,
                                    productCategory = favoriteItem.category
                                )
                            }) {
                            val route =
                                "${Screens.Product.route}/${Uri.encode(favoriteItem.category)}/${favoriteItem.id}"
                            navController.navigate(route)
                        }
                    }
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
