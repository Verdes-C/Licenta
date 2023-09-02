package com.facultate.licenta.redux

import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.UserData

data class ApplicationState(
    val authState: AuthState = AuthState.Unauthenticated(),
    val userData: UserData? = null,
    val search: String = "",
    val cartProducts: List<CartItem> = listOf(),
    val favoriteItems: Set<FavoriteItem> = setOf(),
) {

    sealed interface AuthState {
        data object Authenticated : AuthState

        data class Unauthenticated(val errorString: String? = null) : AuthState
    }

}

