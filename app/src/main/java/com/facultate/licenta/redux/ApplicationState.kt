package com.facultate.licenta.redux

import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.favorites.FavoritesItem
import com.facultate.licenta.screens.profile.UserData

data class ApplicationState(
    val authState: AuthState = AuthState.Unauthenticated(),
    val userData: UserData? = null,
    val search: String = "sss",
    val cartProducts: List<CartItem> = emptyList(),
    val favoritesItem: List<FavoritesItem> = emptyList()
) {

    sealed interface AuthState {
        data class Authenticated(val user: String) : AuthState

        data class Unauthenticated(val errorString: String? = null) : AuthState
    }

}

