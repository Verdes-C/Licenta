package com.facultate.licenta.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.Product
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewmodel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: Repository
) : ViewModel() {

    var favoriteItems = MutableStateFlow<List<Product>>(listOf())

    suspend fun getFavoriteItems() {
        val favoriteItemsResults = actions.getFavoriteItems(viewModelScope)
        favoriteItems.value = favoriteItemsResults
    }

    suspend fun removeFromFavorite(productId: String, productCategory: String) {
        val newFavoriteItems = actions.removeItemFromFavorites(
            productId = productId,
            productCategory = productCategory
        )
        favoriteItems.value =
            repository.getFavoriteItems(viewModelScope, favoriteItems = newFavoriteItems)
        actions.removeItemFromFavorites(productId = productId, productCategory = productCategory)
        repository.updateRemoteFavorites(newFavoriteItems = newFavoriteItems)
    }

    suspend fun addToCart(
        productId: String,
        productCategory: String,
        discount: Double,
        quantity: Int
    ) {
        val newCartItems = actions.getCartItems().toMutableList()
        val item = repository.getCartItem(
            cartItem = CartItemShort(
                productId = productId,
                category = productCategory
            ),
            discount = discount,
            quantity = quantity
        )
        val existingItem = newCartItems.find { it.productId == item.productId }

        if (existingItem != null) {
            val index = newCartItems.indexOf(existingItem)
            newCartItems[index] =
                existingItem.copy(productQuantity = existingItem.productQuantity + 1)
        } else {
            newCartItems.add(item)
        }
        actions.updateCart(newCartProducts = newCartItems.toList<CartItem>())
        repository.updateRemoteCart(newCartProducts = newCartItems.toList<CartItem>())
    }


}