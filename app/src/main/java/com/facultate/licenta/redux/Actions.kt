package com.facultate.licenta.redux

import android.util.Log
import com.facultate.licenta.firebase.FirebaseProductRepository
import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.product.Product
import com.facultate.licenta.utils.FavoriteItem
import com.facultate.licenta.utils.MappersTo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Actions @Inject constructor(
    private val store: Store<ApplicationState>,
    private val repository: FirebaseProductRepository
) {

    suspend fun toggleItemInFavorites(
        productId: String,
        productCategory: String
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

    suspend fun getFavoriteItems(viewModelScope: CoroutineScope): List<Product> {
        val favoriteItems = store.read { applicationState ->
            applicationState.favoriteItems
        }

        return repository.getFavoriteItems(
            viewModelScope = viewModelScope,
            favoriteItems = favoriteItems
        )
    }

    suspend fun removeItemFromFavorites(
        productId: String,
        productCategory: String
    ): MutableSet<FavoriteItem> {
        var newFavoriteItems: MutableSet<FavoriteItem> = mutableSetOf()
        val item = FavoriteItem(productId = productId, category = productCategory)
        Log.d("TESTING", "item = ${item.toString()}")
        store.update { applicationState ->
            newFavoriteItems = applicationState.favoriteItems.toMutableSet()
            Log.d("TESTING", newFavoriteItems.toString())
            newFavoriteItems.remove(item)
            Log.d("TESTING", "Once removed: ${newFavoriteItems.toString()}")
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

}