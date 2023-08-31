package com.facultate.licenta.redux

import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.utils.FavoriteItem
import com.facultate.licenta.utils.UserData

data class ApplicationState(
    val authState: AuthState = AuthState.Unauthenticated(),
    val userData: UserData? = null,
    val search: String = "",
    val cartProducts: List<CartItem> = mutableListOf(),
    val favoriteItems: MutableSet<FavoriteItem> = mutableSetOf(),
) {

    sealed interface AuthState {
        data object Authenticated : AuthState

        data class Unauthenticated(val errorString: String? = null) : AuthState
    }

}

