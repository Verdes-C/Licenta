package com.facultate.licenta.redux

import android.util.Log
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.Product
import com.facultate.licenta.model.UserData
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class Actions @Inject constructor(
    private val store: Store<ApplicationState>,
    private val repository: Repository,
) {

    suspend fun updateUserData(userData: UserData?) {
        var newCartItems: MutableList<CartItem> = mutableListOf()
        var newFavoriteItems: MutableSet<FavoriteItem> = mutableSetOf()
        store.update { applicationState ->
            //_ Utilizarea functiei distinct asigura mentinerea ordinii elementelor
            if (applicationState.cartProducts.isNotEmpty()) {
                newCartItems = applicationState.cartProducts.toMutableList()
                userData?.cartItem?.forEach { cartItem ->
                    val existingItem = newCartItems.find { it.productId == cartItem.productId }
                    if (existingItem != null) {
                        cartItem.productQuantity += existingItem.productQuantity
                        newCartItems.remove(existingItem)
                    }
                    newCartItems.add(cartItem)
                }
            } else {
                newCartItems = userData?.cartItem?.toMutableList() ?: mutableListOf()
            }
            if (applicationState.favoriteItems.isNotEmpty()) {
                newFavoriteItems =
                    (applicationState.favoriteItems.toMutableSet() + (userData?.favoriteItems
                        ?: emptySet())).distinct().toMutableSet()
            } else {
                newFavoriteItems = userData?.favoriteItems?.toMutableSet() ?: mutableSetOf()
            }
            return@update applicationState.copy(
                authState = ApplicationState.AuthState.Authenticated,
                userData = userData,
                favoriteItems = newFavoriteItems.toSet(),
                cartProducts = newCartItems
            )
        }

    }

    suspend fun toggleItemInFavorites(
        productId: String,
        productCategory: String,
    ): MutableSet<FavoriteItem> {
        var newFavoriteItems: MutableSet<FavoriteItem> = mutableSetOf()

        store.update { applicationState ->
            newFavoriteItems = applicationState.favoriteItems.toMutableSet()

            val item = FavoriteItem(productId = productId, category = productCategory)

            if (newFavoriteItems.contains(item)) {
                newFavoriteItems.remove(item)
            } else {
                newFavoriteItems.add(item)
            }

            applicationState.copy(favoriteItems = newFavoriteItems)
        }
        return newFavoriteItems
    }

    suspend fun getFavoriteItems(): Set<FavoriteItem> {
        return store.read { it.favoriteItems }
    }

    suspend fun removeItemFromFavorites(
        productId: String,
        productCategory: String,
    ): MutableSet<FavoriteItem> {
        var newFavoriteItems: MutableSet<FavoriteItem> = mutableSetOf()
        val item = FavoriteItem(productId = productId, category = productCategory)
        store.update { applicationState ->
            newFavoriteItems = applicationState.favoriteItems.toMutableSet()
            newFavoriteItems.remove(item)
            return@update applicationState.copy(
                favoriteItems = newFavoriteItems
            )
        }
        return newFavoriteItems
    }

    suspend fun getCartItems(): List<CartItem> {
        return store.read { applicationState ->
            return@read applicationState.cartProducts
        }
    }

    suspend fun updateCart(newCartProducts: List<CartItem>) {
        store.update { applicationState ->
            return@update applicationState.copy(
                cartProducts = newCartProducts
            )
        }
    }

    suspend fun updateItemFromCart(productId: String, quantity: Int): List<CartItem> {
        var newCartProducts = mutableListOf<CartItem>()
        store.update { applicationState ->
            newCartProducts = applicationState.cartProducts.toMutableList<CartItem>()
            val index = newCartProducts.indexOfFirst {
                it.productId == productId
            }
            if (index != -1) {
                val updatedItem = newCartProducts[index].copy(productQuantity = quantity)
                (newCartProducts)[index] = updatedItem
            }
            return@update applicationState.copy(
                cartProducts = newCartProducts
            )
        }
        return newCartProducts
    }

    suspend fun removeItemFromCart(productId: String, productCategory: String): List<CartItem> {
        var newCartItems = listOf<CartItem>()
        store.update { applicationState ->
            val cartItems = applicationState.cartProducts.toMutableList()
            newCartItems = cartItems.filter {
                it.productId != productId
            }
            return@update applicationState.copy(
                cartProducts = newCartItems
            )
        }
        return newCartItems
    }

    suspend fun clearCart() {
        store.update { applicationState ->
            return@update applicationState.copy(
                cartProducts = listOf<CartItem>()
            )
        }
    }

    suspend fun signOut() {
        store.update { applicationState ->
            return@update applicationState.copy(
                authState = ApplicationState.AuthState.Unauthenticated(),
                userData = UserData(email = ""),
                cartProducts = emptyList(),
                favoriteItems = emptySet()
            )
        }
    }

    suspend fun saveOrderAndClear(newOrder: Order) {
        store.update { applicationState ->
            val orders = applicationState.orders.toMutableList()
            orders.add(newOrder)
            return@update applicationState.copy(
                orders = orders,
            )
        }
    }

    suspend fun checkIfProductIsFavorite(productId: String): Boolean {
        return store.read { applicationState ->
            applicationState.favoriteItems.any { it.productId == productId }
        }
    }

}