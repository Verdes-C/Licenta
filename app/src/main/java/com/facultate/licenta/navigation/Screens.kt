package com.facultate.licenta.navigation

import androidx.annotation.StringRes
import com.facultate.licenta.R

sealed class Screens(val route: String, @StringRes val resourceId: Int) {
    //_ Base locations
    object HomePage : Screens(route = "homepage", resourceId = R.string.homepage)
    object Categories : Screens(route = "categories", resourceId = R.string.categories)
    object Cart : Screens(route = "cart", resourceId = R.string.cart)
    object Favorites : Screens(route = "favorites", resourceId = R.string.favorites)
    object Product : Screens(route = "product", resourceId = R.string.product)

    //_ Nested Graph for Profile
    object ProfileGraph : Screens(route = "profileGraph", resourceId = R.string.profile)

    //? Initial Destination
    object Profile : Screens(route = "profileGraph/profileHome", resourceId = R.string.profile)

    //? Nested locations
    object Orders : Screens(route = "profileGraph/orders", resourceId = R.string.orders)
    object Vouchers : Screens(route = "profileGraph/vouchers", resourceId = R.string.vouchers)
    object AccountData : Screens(route = "profileGraph/accountData", resourceId = R.string.accountData)

}