package com.facultate.licenta.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.facultate.licenta.components.HomeScreenDisplaySection
import com.facultate.licenta.components.HomeScreenProductDisplay
import com.facultate.licenta.components.SearchBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables


@Composable
fun HomePage(
    navController: NavHostController,
    viewModel: HomePageViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {

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
            HomeScreenDisplaySection(
                displayText = "Promotions",
                textStyle = Typography.h2,
                showMoreOnClick = {
                    //_ Navigate to promotions
                }) {
                item { HomeScreenProductDisplay(isSale = true, navController = navController) }
                repeat(7) {
                    item {
                        HomeScreenProductDisplay(
                            isSale = false,
                            navController = navController
                        )
                    }
                }
            }
        }

        item {
            HomeScreenDisplaySection(displayText = "New arrivals", showMoreOnClick = {
                //_ Navigate to new arrivals
            }) {
                item { HomeScreenProductDisplay(isSale = false, navController = navController) }
                repeat(7) {
                    item {
                        HomeScreenProductDisplay(
                            isSale = false,
                            navController = navController
                        )
                    }
                }
            }
        }

        item {
            HomeScreenDisplaySection(displayText = "Find something new", showMoreOnClick = {
                //_ Navigate to discover
            }) {
                item { HomeScreenProductDisplay(isSale = false, navController = navController) }
                repeat(7) {
                    item {
                        HomeScreenProductDisplay(
                            isSale = false,
                            navController = navController
                        )
                    }
                }
            }
        }

        item {
            HomeScreenDisplaySection(displayText = "Some other section", showMoreOnClick = {

            }) {
                item { HomeScreenProductDisplay(isSale = false, navController = navController) }
                repeat(7) {
                    item {
                        HomeScreenProductDisplay(
                            isSale = false,
                            navController = navController
                        )
                    }
                }
            }
        }
        item {
            HomeScreenDisplaySection(displayText = "Some other section", showMoreOnClick = {

            }) {
                item { HomeScreenProductDisplay(isSale = false, navController = navController) }
                repeat(7) {
                    item {
                        HomeScreenProductDisplay(
                            isSale = false,
                            navController = navController
                        )
                    }
                }
            }
        }
        item {
            HomeScreenDisplaySection(displayText = "Some other section", showMoreOnClick = {

            }) {
                item { HomeScreenProductDisplay(isSale = false, navController = navController) }
                repeat(7) {
                    item {
                        HomeScreenProductDisplay(
                            isSale = false,
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}



