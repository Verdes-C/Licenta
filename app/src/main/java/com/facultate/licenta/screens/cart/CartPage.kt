package com.facultate.licenta.screens.cart

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.DisplayCartItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.Utils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPage(
    navController: NavHostController,
    viewModel: CartPageViewModel = hiltViewModel(),
) {

    val cartItems by viewModel.cartProducts.collectAsState()
    val favoriteItems by viewModel.favoriteItems.collectAsState()

    if (cartItems.isEmpty()) {
        LaunchedEffect(key1 = cartItems) {
            viewModel.viewModelScope.launch {
                viewModel.updateCartProducts()
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.Cart,
                navController = navController,
            )
            {}
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Variables.grey1)
                    .padding(
                        horizontal = Variables.outerItemGap,
                        vertical = Variables.innerItemGapLow
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total: $${Utils.calculateCartTotalPrice(cartList = cartItems)}",
                        style = Typography.h4,
                        color = Variables.blue3
                    )
                    Buttons.SecondaryActive(text = "Clear cart") {
                        viewModel.viewModelScope.launch {
                            viewModel.clearCart()
                        }
                    }
                }
                Buttons.PrimaryActive(text = "Order", modifier = Modifier.fillMaxWidth()) {
                    viewModel.order()
                }
            }
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
            if (cartItems.isEmpty()) {
                item {
                    DisplayEmptyCart()
                }
            } else {
                items(items = cartItems) { cartItem ->
                    // TODO modify to take CartItem
                    DisplayCartItem(
                        cartItem = cartItem,
                        modifier = Modifier,
                        viewModel = viewModel,
                        isFavorite = favoriteItems.contains(
                            FavoriteItem(
                                productId = cartItem.productId,
                                category = cartItem.productCategory
                            )
                        )
                    ) {
                        val route =
                            "${Screens.Product.route}/${Uri.encode(cartItem.productCategory)}/${cartItem.productId}"
                        navController.navigate(route)
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayEmptyCart() {
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
            painter = painterResource(id = R.drawable.icon_empty_cart),
            contentDescription = "Empty cart icon",
            tint = Variables.blue3,
            modifier = Modifier.padding(bottom = Variables.innerItemGap)
        )
    }
}

