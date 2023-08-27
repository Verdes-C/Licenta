package com.facultate.licenta.navigation

import androidx.annotation.StringRes
import com.facultate.licenta.R

sealed class Screens(val route: String, @StringRes val resourceId: Int) {
    object HomePage : Screens("homepage", R.string.homepage)
    object Categories : Screens("categories", R.string.categories)
    object Cart : Screens("cart", R.string.cart)
    object Favorites : Screens("favorites", R.string.favorites)
    object Profile : Screens("profile", R.string.profile)
    object ProfileGraph : Screens("profileGraph", R.string.profile)
    object Product : Screens("product", R.string.product)
}