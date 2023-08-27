package com.facultate.licenta.Screens.cart

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.DisplayCartItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPage(navController: NavHostController) {

    // TODO actualy storage
    val cartItems = listOf<CartItem>(
        CartItem(),
        CartItem(productQuantity = 5),
        CartItem(productQuantity = 65),
        CartItem(productQuantity = 25),
        CartItem(productQuantity = 15),
        CartItem(productQuantity = 7),
        CartItem(productQuantity = 4),
        CartItem(productQuantity = 35)
    )

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.Cart,
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
            if (cartItems.isEmpty()) {
                item {
                    DisplayEmptyCart()
                }
            } else {
                items(items = cartItems) { cartItem ->
                    // TODO modify to take CartItem
                    DisplayCartItem(
                        cartItem = cartItem,
                        modifier = Modifier.padding(bottom = Variables.innerItemGap)
                    )
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
            color = Variables.blue3,
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

data class CartItem(
//    TODO remove defaults other than quantity
    val productId: String = "123123123",
    val productName: String = "ProductName",
    val productImage: Int = R.drawable.image_placeholder,
    val productImageDescription: String = "Product image description",
    val productPrice: Double = 123.24,
    var productQuantity: Int = 1,
)