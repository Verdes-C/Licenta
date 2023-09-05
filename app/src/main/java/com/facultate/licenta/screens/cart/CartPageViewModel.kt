package com.facultate.licenta.screens.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.Order
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CartPageViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: FirebaseRepository,
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

    fun order() = viewModelScope.launch {
        val email = store.read { it.userData?.email }
        if (email.isNullOrEmpty()) {
            //todo
            return@launch
        }
        val fullAddress = store.read { it.userData }?.let { Utils.getFullAdress(userData = it) }
        if (fullAddress.isNullOrEmpty()) {
            //todo
        }

        val newOrder = Order(
            userEmail = email,
            orderNumber = UUID.randomUUID(),
            totalPrice = Utils.calculateCartTotalPrice(cartList = cartProducts.value),
            products = cartProducts.value,
            fullAddress = fullAddress!!
        )
        try {
            async {repository.saveOrder(newOrder = newOrder, email = email)}.await()
            async { actions.saveOrderAndClear(newOrder = newOrder) }.await()
            clearCart()
        } catch (e: Exception) {
            //
        }
    }

}