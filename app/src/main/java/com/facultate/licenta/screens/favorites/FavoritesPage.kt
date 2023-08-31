package com.facultate.licenta.screens.favorites

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.facultate.licenta.MainActivityViewModel
import com.facultate.licenta.R
import com.facultate.licenta.components.DisplayFavoriteItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlinx.coroutines.launch
import okhttp3.internal.checkDuration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesPage(
    navController: NavHostController,
    viewModel: FavoritesViewmodel = hiltViewModel(),
    sharedViewModel: MainActivityViewModel = hiltViewModel(),
) {

    val favoriteItems by viewModel.favoriteItems.collectAsState()

    var finishedLoading by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.viewModelScope.launch {
            viewModel.getFavoriteItems()
            finishedLoading = true
        }
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
            if (finishedLoading && favoriteItems.isEmpty()) {
                item {
                    DisplayEmptyFavorites()
                }
            } else if (finishedLoading && favoriteItems.isNotEmpty()) {
                items(items = favoriteItems) { favoriteItem ->
                    DisplayFavoriteItem(
                        modifier = Modifier,
                        favoritesItem = favoriteItem,
                        addToCart = {
                            viewModel.viewModelScope.launch {
                                viewModel.addToCart(
                                    productId = favoriteItem.id,
                                    productCategory = favoriteItem.category,
                                    discount = favoriteItem.discount,
                                    quantity = 1
                                )
                            }
                        },
                        removeFromFavorite = {
                            viewModel.viewModelScope.launch {
                                viewModel.removeFromFavorite(
                                    productId = favoriteItem.id,
                                    productCategory = favoriteItem.category
                                )
                            }
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
