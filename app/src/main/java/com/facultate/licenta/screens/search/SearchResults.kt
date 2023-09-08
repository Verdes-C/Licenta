package com.facultate.licenta.screens.search

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.DisplayLoading
import com.facultate.licenta.components.DisplaySearchResult
import com.facultate.licenta.components.MenuEntry
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.Product
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.screens.home.HomePageViewModel
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResults(
    navController: NavHostController,
    category: String?,
    inputQuery: String = "",
    viewModel: SearchResultsViewModel = hiltViewModel(),
    homePageViewModel: HomePageViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val searchResultProducts by viewModel.searchResults.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getResults(category = category, searchInput = inputQuery)
    }

    Scaffold(
        topBar = {
            SearchBar(
                modifier = Modifier.padding(horizontal = Variables.outerItemGap),
                initialValue = inputQuery.ifEmpty { category ?: "" }) { searchQuery ->
                if (searchQuery.isNotEmpty() && searchQuery.replace(" ", "") != "") {
                    val route = "search/null/${Uri.encode(searchQuery)}"
                    navController.navigate(route)
                } else {
                    Toast.makeText(context, "Please input a valid search value", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    ) { paddingValues ->

        if (searchResultProducts is DataState.Success) {
            val results = (searchResultProducts as DataState.Success<List<Product>>).data
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Variables.grey1)
                    .padding(paddingValues = paddingValues)
                    .padding(horizontal = Variables.outerItemGap)
                    .padding(top = Variables.outerItemGap),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                items(items = results) { product ->
                    DisplaySearchResult(
                        productImageDescription = product.description,
                        productName = product.name,
                        productId = product.id,
                        productCategory = product.category,
                        productImage = product.images.first(),
                        productPrice = product.price,
                        discount = product.discount,
                        viewModel = homePageViewModel
                    ) {
                        val route =
                            "${Screens.Product.route}/${Uri.encode(product.category)}/${product.id}"
                        navController.navigate(route)
                    }
                }
            }
        } else if (searchResultProducts is DataState.Loading) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DisplayLoading()
            }
        } else if (searchResultProducts is DataState.Error) {
            val error = (searchResultProducts as DataState.Error).exception
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                error.message?.let { Text(text = it, style = Typography.pBig) }
            }
        }
    }
}


