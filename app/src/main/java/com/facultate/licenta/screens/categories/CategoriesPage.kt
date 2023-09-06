package com.facultate.licenta.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.CategoryCard
import com.facultate.licenta.components.CategorySection
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import java.util.Locale
import kotlin.math.ceil

@Composable
fun CategoriesPage(
    navController: NavHostController,
    viewModel: CategoriesViewModel = hiltViewModel(),
) {

    val artFountainPensList = listOf(
        "Flexible nib", "Art brush", "Italic & stub nib"
    )
    val calligraphyList = listOf(
        "Calligraphy brush", "Calligraphy dip pen", "Calligraphy fountain pen", "Calligraphy nib"
    )
    val accessoriesList = listOf(
        "Cardridges", "Ink"
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Variables.grey1)
            .padding(horizontal = Variables.outerItemGap)
            .padding(bottom = Variables.outerItemGap),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            SearchBar(){}
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Art fountain pens",
                    style = Typography.h4,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(
                        Variables.innerItemGap,
                        Alignment.CenterVertically
                    ),
                    horizontalArrangement = Arrangement.spacedBy(
                        Variables.innerItemGap,
                        Alignment.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((ceil((artFountainPensList.size / 3.0)) * 112).dp)
                ) {
                    items(items = artFountainPensList) { category ->
                        CategoryCard(categoryName = category) {
                            val route = calculateRoute(category = category)
                            navController.navigate(route)
                        }
                    }
                }
            }
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Calligraphy",
                    style = Typography.h4,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(
                        Variables.innerItemGap,
                        Alignment.CenterVertically
                    ),
                    horizontalArrangement = Arrangement.spacedBy(
                        Variables.innerItemGap,
                        Alignment.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((ceil((calligraphyList.size / 3.0)) * 112).dp)
                ) {
                    items(items = calligraphyList) { category ->
                        CategoryCard(categoryName = category) {
                            val route = calculateRoute(category = category)
                            navController.navigate(route)
                        }
                    }
                }
            }
        }
//? accessories
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Accessories",
                    style = Typography.h4,
                    modifier = Modifier
                        .wrapContentHeight(align = Alignment.CenterVertically)
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(
                        Variables.innerItemGap,
                        Alignment.CenterVertically
                    ),
                    horizontalArrangement = Arrangement.spacedBy(
                        Variables.innerItemGap,
                        Alignment.Start
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((ceil((accessoriesList.size / 3.0)) * 112).dp)
                ) {
                    items(items = accessoriesList) { category ->
                        CategoryCard(categoryName = category) {
                            val route = calculateRoute(category = category)
                            navController.navigate(route)
                        }
                    }
                }
            }
        }
    }
}

fun calculateRoute(category: String): String{
    return "search/${category.split(" ").map {
        it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }.joinToString(" ")}/null"
}