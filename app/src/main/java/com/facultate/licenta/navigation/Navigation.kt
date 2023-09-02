package com.facultate.licenta.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navigation
import com.facultate.licenta.screens.cart.CartPage
import com.facultate.licenta.screens.categories.CategoriesPage
import com.facultate.licenta.screens.favorites.FavoritesPage
import com.facultate.licenta.screens.home.HomePage
import com.facultate.licenta.screens.product.ProductPage
import com.facultate.licenta.screens.profile.AccountDataPage
import com.facultate.licenta.screens.profile.Orders
import com.facultate.licenta.screens.profile.ProfileHomePage
import com.facultate.licenta.screens.profile.RegisterNewUser
import com.facultate.licenta.screens.profile.ResetPassword
import com.facultate.licenta.screens.profile.VouchersPage
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun NavHost(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Screens.HomePage.route,
        modifier = Modifier.padding(innerPadding)
    ) {

        composable(route = Screens.Categories.route) { backStackEntry ->
            CategoriesPage(navController = navController)
        }

        composable("product/{category}/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            val productCategory = Uri.decode(backStackEntry.arguments?.getString("category"))
            if (productId != null && productCategory != null) {
                ProductPage(navController, productId, productCategory)
            } else {
                //! Handle error
            }
        }

        composable(route = Screens.HomePage.route) { backStackEntry ->
            HomePage(navController = navController)
        }


        composable(route = Screens.Cart.route) { backStackEntry ->
            CartPage(navController = navController)
        }

        composable(route = Screens.Favorites.route) { backStackEntry ->
            FavoritesPage(navController = navController)
        }

        composable(route = Screens.ResetPassword.route) { backStackEntry ->
            ResetPassword(navController = navController)
        }

        navigation(
            startDestination = Screens.Profile.route,
            route = "profileGraph"
        ) {
            composable(Screens.Profile.route) { backStackEntry ->
                ProfileHomePage(navController = navController)
            }

            composable(Screens.Orders.route) { backStackEntry ->
                Orders(navController = navController)
            }

            composable(Screens.Vouchers.route) { backStackEntry ->
                VouchersPage(navController = navController)
            }

            composable(Screens.AccountData.route) { backStackEntry ->
                AccountDataPage(navController = navController)
            }

            composable(Screens.RegisterUser.route) { backStackEntry ->
                RegisterNewUser(navController = navController)
            }

        }

//            composable("${Screens.Product.route}/{productId}/${Screens.Reviews.route}") { backStackEntry ->
//                val productId = backStackEntry.arguments?.getString("productId")
//                if (productId != null) {
//                    val productBackStackEntry = navController.currentBackStackEntry
//                    val productArgs = productBackStackEntry?.arguments
//                    val reviewsRoute = productArgs?.getString("productId")?.let {
//                        "${Screens.Product.route}/$it/${Screens.Reviews.route}"
//                    }
//
//                    if (reviewsRoute != null) {
//                        ReviewsPage(navController)
//                    } else {
//                        //! Handle error
//                    }
//                } else {
//                    //! Handle error
//                }
//            }
//        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNav(screens: List<Pair<Screens, ImageVector>>, navController: NavHostController) {
    Scaffold(
        bottomBar = {
            Column {
                //_ In order to add the border top
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .border(width = 1.dp, color = Variables.orange1)
                )
                BottomNavigation(
                    modifier = Modifier,
                    backgroundColor = Variables.grey1,
                    contentColor = Variables.grey6,
                    elevation = Variables.elevation
                ) {
                    //_ Get the selected item in the bottom nav trough the destination of the navController
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

                    screens.forEach { screen ->
                        //_ For each defined screen we profide and icon with a description
                        BottomNavigationItem(
                            modifier = Modifier,
                            alwaysShowLabel = false,
                            unselectedContentColor = Variables.grey6,
                            icon = {
                                Icon(
                                    imageVector = screen.second,
                                    contentDescription = stringResource(id = screen.first.resourceId),
                                    modifier = Modifier.size(32.dp)
                                )
                            },
                            label = {
                                Text(
                                    stringResource(screen.first.resourceId),
                                    style = Typography.buttonBold
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.first.route } == true ||
                                    (screen.first == Screens.Profile && (currentDestination?.hierarchy?.any {
                                        it.route?.startsWith(
                                            "profileGraph"
                                        ) == true
                                    } == true)) ||

                                    (screen.first == Screens.HomePage && (currentDestination?.hierarchy?.any {
                                        it.route?.startsWith(
                                            "product"
                                        ) == true
                                    } == true)),
                            onClick = {
                                navController.navigate(screen.first.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, innerPadding = innerPadding)
    }
}