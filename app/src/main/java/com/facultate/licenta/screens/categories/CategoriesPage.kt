package com.facultate.licenta.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.CategorySection
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.ui.theme.Variables

@Composable
fun CategoriesPage(
    navController: NavHostController,
    viewModel: CategoriesViewModel = hiltViewModel(),
) {

    val category1List = listOf(
        "Flexible nib", "Art brush", "Italic & stub nib"
    )
    val category2List = listOf(
        "Calligraphy brush", "Calligraphy dip pen", "Calligraphy fountain pen", "Calligraphy nib"
    )
    val category3List = listOf(
        "Cardridges", "Ink"
    )
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
        item {
            CategorySection(categoryName = "Art fountain pens", categoryList = category1List)
        }
        item {
            CategorySection(categoryName = "Calligraphy", categoryList = category2List)
        }
//? accessories
        item {
            CategorySection(categoryName = "Accessories", categoryList = category3List)
        }
    }
}

