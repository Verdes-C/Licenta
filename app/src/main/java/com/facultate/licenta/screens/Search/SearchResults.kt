package com.facultate.licenta.screens.Search

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.HomeScreenProductDisplay
import com.facultate.licenta.components.MenuEntry
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.screens.home.HomePageViewModel
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResults(
    navController: NavHostController,
    category: String,
    viewModel: SearchResultsViewModel = hiltViewModel(),
    homePageViewModel: HomePageViewModel = hiltViewModel(),
) {

    val searchResultProducts by viewModel.searchResults.collectAsState()
    Log.d(
        "TESTING", "${searchResultProducts}"
    )

    LaunchedEffect(Unit) {
        viewModel.getResults(category = category)
    }

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntry(
                    name = "Results",
                    icon = R.drawable.material_symbols_search,
                    iconDescription = "Search results"
                ),
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
            items(items = searchResultProducts) { product ->
                HomeScreenProductDisplay(
                    productImageDescription = product.description,
                    productName = product.name,
                    productId = product.id,
                    productCategory = product.category,
                    productImage = product.images.first(),
                    productPrice = product.price,
                    discount = 0.0,
                    viewModel = homePageViewModel
                ) {
                    val route =
                        "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                    navController.navigate(route)
                }
            }
        }

    }
}
