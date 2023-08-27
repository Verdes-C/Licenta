package com.facultate.licenta.Screens.categories

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.CategoryCard
import com.facultate.licenta.components.CategorySection
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlin.math.ceil

@Composable
fun CategoriesPage(navController: NavHostController) {

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
            Column(
                verticalArrangement = Arrangement.spacedBy(Variables.outerItemGap, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Calligraphy",
                    color = Color(0xff163688),
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
                        .height((ceil((category2List.size / 3.0)) * 112).dp)
                ) {
                    items(items = category2List) { category ->
                        CategoryCard(categoryName = category)
                    }
                }
            }
        }
//? accessories
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(Variables.outerItemGap, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Accessories",
                    color = Color(0xff163688),
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
                        .height((ceil((category3List.size / 3.0)) * 112).dp)
                ) {
                    items(items = category3List) { category ->
                        CategoryCard(categoryName = category)
                    }
                }
            }
        }
    }
}

