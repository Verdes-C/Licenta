package com.facultate.licenta.navigation

import android.app.Activity.RESULT_OK
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.facultate.licenta.firebase.GoogleAuthUiClient
import com.facultate.licenta.screens.search.SearchResults
import com.facultate.licenta.screens.admins.AdminOrdersPage
import com.facultate.licenta.screens.cart.CartPage
import com.facultate.licenta.screens.categories.CategoriesPage
import com.facultate.licenta.screens.favorites.FavoritesPage
import com.facultate.licenta.screens.home.HomePage
import com.facultate.licenta.screens.product.ProductPage
import com.facultate.licenta.screens.profile.AccountDataPage
import com.facultate.licenta.screens.profile.Orders
import com.facultate.licenta.screens.profile.ProfileHomePage
import com.facultate.licenta.screens.profile.ProfileViewModel
import com.facultate.licenta.screens.profile.RegisterNewUser
import com.facultate.licenta.screens.profile.ResetPassword
import com.facultate.licenta.screens.profile.VouchersPage
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlinx.coroutines.launch

@Composable
fun NavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    lifeCycleScope: LifecycleCoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient,
    profileViewModel: ProfileViewModel,
) {


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
                val isAuth = profileViewModel.isAuth.collectAsStateWithLifecycle()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifeCycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                profileViewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )
                ProfileHomePage(navController = navController, logout = {
                    lifeCycleScope.launch {
                        googleAuthUiClient.signOut()
                    }
                }) {
                    lifeCycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            }

            composable(Screens.Orders.route,
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "inkquill://${Screens.Orders.route}"
                    }
                )
            ) { backStackEntry ->

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

            composable(Screens.AdminOrders.route) { backStackEntry ->
                AdminOrdersPage(navController = navController)
            }

        }

        composable("search/{category}/{inputQuery}") { backStackEntry ->
            val category = Uri.decode(backStackEntry.arguments?.getString("category"))
            if (category != "null") {
                SearchResults(navController, category)
            } else {
                val inputQuery = Uri.decode(backStackEntry.arguments?.getString("inputQuery"))
                SearchResults(
                    navController = navController,
                    category = null,
                    inputQuery = inputQuery
                )
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
fun BottomNav(
    screens: List<Pair<Screens, ImageVector>>,
    navController: NavHostController,
    lifecycleScope: LifecycleCoroutineScope,
    googleAuthUiClient: GoogleAuthUiClient,
    profileViewModel: ProfileViewModel,
) {
    val cartCount by profileViewModel.cartCount.collectAsState()

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
                                Box(modifier = Modifier.size(40.dp)) {
                                    Icon(
                                        imageVector = screen.second,
                                        contentDescription = stringResource(id = screen.first.resourceId),
                                        modifier = Modifier
                                            .size(32.dp)
                                            .align(Alignment.Center)
                                    )
                                    if (screen.first == Screens.Cart && cartCount > 0) {
                                        Text(
                                            text = "$cartCount", style = Typography.h4.copy(
                                                color = Variables.red,
                                            ),
                                            modifier = Modifier
                                                .align(Alignment.TopEnd)
                                                .offset(x = (5).dp, y = (-5).dp)
                                        )
                                    }
                                }
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
        NavHost(
            navController = navController,
            innerPadding = innerPadding,
            lifeCycleScope = lifecycleScope,
            googleAuthUiClient = googleAuthUiClient,
            profileViewModel = profileViewModel
        )
    }
}