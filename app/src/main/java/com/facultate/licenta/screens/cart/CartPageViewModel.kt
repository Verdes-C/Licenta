package com.facultate.licenta.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.hilt.interfaces.ProductRepository
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.FavoriteItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CartPageViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: ProductRepository
) : ViewModel() {

    val cartProducts = MutableStateFlow(listOf<CartItem>())
    val favoriteItems = MutableStateFlow(store.stateFlow.value.favoriteItems)

    suspend fun updateCartProducts() {
        cartProducts.value = actions.getCartItems()
    }

    suspend fun clearCart() {
        actions.clearCart()
        cartProducts.value = listOf<CartItem>()
        repository.updateRemoteCart(newCartProducts = listOf<CartItem>())
    }

    suspend fun addOrRemoveFromFavorites(productId: String, productCategory: String) {
        val newFavoriteProducts =
            actions.toggleItemInFavorites(productId = productId, productCategory = productCategory)
        repository.updateRemoteFavorites(newFavoriteItems = newFavoriteProducts)
        favoriteItems.value = newFavoriteProducts
    }

    suspend fun removeFromCart(productId: String, productCategory: String) {
        val newCartProducts =
            actions.removeItemFromCart(productId = productId, productCategory = productCategory)
        repository.updateRemoteCart(newCartProducts = newCartProducts)
        cartProducts.value = newCartProducts
    }

    suspend fun updateCartProduct(productId: String, quantity: Int) {
        val newCartProducts = actions.updateItemFromCart(productId = productId, quantity = quantity)
        repository.updateRemoteCart(newCartProducts = newCartProducts)
        cartProducts.value = newCartProducts
        cartProducts.value = newCartProducts
    }

}