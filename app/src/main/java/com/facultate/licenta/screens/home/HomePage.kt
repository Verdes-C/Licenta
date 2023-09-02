package com.facultate.licenta.screens.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.HomeScreenDisplaySection
import com.facultate.licenta.components.HomeScreenProductDisplay
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.model.Product
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Variables


@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: HomePageViewModel = hiltViewModel(),
) {
    val promotions by viewModel.promotions.collectAsState()
    val newArrivals by viewModel.newArrivals.collectAsState()
    val findNew by viewModel.findNew.collectAsState()

    when {
        promotions is DataState.Loading ||
                newArrivals is DataState.Loading ||
                findNew is DataState.Loading -> {
            LaunchedEffect(Unit) {
                viewModel.loadHomeData()
            }
        }

        promotions is DataState.Success &&
                newArrivals is DataState.Success &&
                findNew is DataState.Success -> {
            // Display the content
            // You can get the data by casting:
            // (promotions as DataState.Success).data
            //_ Unwrap the data
            val promotionsList = (promotions as DataState.Success<List<Product>>).data
            val newArrivalsList = (newArrivals as DataState.Success<List<Product>>).data
            val findNewList = (findNew as DataState.Success<List<Product>>).data

            //_ Display UI
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Variables.grey1)
                    .padding(horizontal = Variables.outerItemGap),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                item {
                    SearchBar()
                }
                //_ Promotions section
                item {
                    ProductSection(
                        title = "Promotions",
                        products = promotionsList,
                        onShowMoreClick = {
                            // Handle show more click for promotions
                        },
                        viewModel = viewModel,
                        onProductClick = { product ->
                            val route =
                                "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                            navController.navigate(route)
                        }
                    )
                }

                item {
                    ProductSection(
                        title = "New arrivals",
                        products = newArrivalsList,
                        onShowMoreClick = {
                            // Handle show more click for new arrivals
                        },
                        viewModel = viewModel,
                        onProductClick = { product ->
                            val route =
                                "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                            navController.navigate(route)
                        }
                    )
                }

                item {
                    ProductSection(
                        title = "Find something new",
                        products = findNewList,
                        onShowMoreClick = {
                            // Handle show more click for new arrivals
                        },
                        viewModel = viewModel,
                        onProductClick = { product ->
                            val route =
                                "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                            navController.navigate(route)
                        }
                    )
                }
            }
        }

        else -> {
            // Handle the error case, one or more of your data states is in error state.
        }
    }


}

@Composable
fun ProductSection(
    title: String,
    products: List<Product>,
    onShowMoreClick: () -> Unit,
    viewModel: HomePageViewModel,
    onProductClick: (Product) -> Unit
) {
    HomeScreenDisplaySection(displayText = title, showMoreOnClick = onShowMoreClick) {
        items(items = products) { product ->
            HomeScreenProductDisplay(
                productImageDescription = product.description,
                productName = product.name,
                productId = product.id,
                productCategory = product.category,
                productImage = product.images.first(),
                productPrice = product.price,
                discount = product.discount,
                viewModel = viewModel,
            ) {
                onProductClick(product)
            }
        }
    }
}


