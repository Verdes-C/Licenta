package com.facultate.licenta.screens.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.DisplayLoading
import com.facultate.licenta.components.HomeScreenDisplaySection
import com.facultate.licenta.components.HomeScreenProductDisplay
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.Product
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: HomePageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val promotions by viewModel.promotions.collectAsState()
    val newArrivals by viewModel.newArrivals.collectAsState()
    val findNew by viewModel.findNew.collectAsState()

    LaunchedEffect(key1 = Unit) {
        if ((promotions is DataState.Loading) || (newArrivals is DataState.Loading) || (findNew is DataState.Loading)) {
            viewModel.loadHomeData()
        }
    }

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
            SearchBar() { searchQuery ->
                if (searchQuery.isNotEmpty() && searchQuery.replace(" ", "") != "") {
                    val route = "search/null/${Uri.encode(searchQuery)}"
                    navController.navigate(route)
                } else {
                    Toast.makeText(
                        context,
                        "Please input a valid search value",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        //_ Promotions section
        item {
            if (promotions is DataState.Success) {
                val promotionsList = (promotions as DataState.Success<List<Product>>).data
                ProductSection(
                    title = "Promotions",
                    products = promotionsList,
                    onShowMoreClick = {
                        val route = "search/${Uri.encode("Promotions")}/null"
                        navController.navigate(route)
                    },
                    viewModel = viewModel,
                    onProductClick = { product ->
                        val route =
                            "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                        navController.navigate(route)
                    }
                )
            } else {
                DisplayLoadingHomeSection(sectionName = "Promotions")
            }
        }

        //_ New Arrivals section
        item {
            if (newArrivals is DataState.Success) {
                val newArrivalsList = (newArrivals as DataState.Success<List<Product>>).data
                ProductSection(
                    title = "New arrivals",
                    products = newArrivalsList,
                    onShowMoreClick = {
                        val route = "search/${Uri.encode("New Arrivals")}/null"
                        navController.navigate(route)
                    },
                    viewModel = viewModel,
                    onProductClick = { product ->
                        val route =
                            "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                        navController.navigate(route)
                    }
                )
            } else {
                DisplayLoadingHomeSection(sectionName = "New arrivals")
            }
        }

        //_ Find Something New section
        item {
            if (findNew is DataState.Success) {
                val findNewList = (findNew as DataState.Success<List<Product>>).data
                ProductSection(
                    title = "Find something new",
                    products = findNewList,
                    onShowMoreClick = {
                        val route = "search/${Uri.encode("Find Something New")}/null"
                        navController.navigate(route)
                    },
                    viewModel = viewModel,
                    onProductClick = { product ->
                        val route =
                            "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                        navController.navigate(route)
                    }
                )
            } else {
                DisplayLoadingHomeSection(sectionName = "Find something new")
            }
        }
    }
}

@Composable
fun DisplayLoadingHomeSection(sectionName: String, onShowMoreClick: () -> Unit = {}) {
    Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = sectionName,
            style = Typography.h3,
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
                .weight(1f)
        )
        Text(
            text = "Show more",
            color = Variables.black,
            style = Typography.caption,
            modifier = Modifier
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable {
                    onShowMoreClick.invoke()
                }
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DisplayLoading()
    }
}


@Composable
fun ProductSection(
    title: String,
    products: List<Product>,
    onShowMoreClick: () -> Unit,
    viewModel: HomePageViewModel,
    onProductClick: (Product) -> Unit,
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


