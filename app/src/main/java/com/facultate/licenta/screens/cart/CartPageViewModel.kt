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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    val cartProducts = store.stateFlow.map { it.cartProducts }.distinctUntilChanged().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = listOf()
    )
    val favoriteItems = store.stateFlow.map { it.favoriteItems }.distinctUntilChanged().stateIn(
        scope = viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = setOf()
    )

    fun clearCart() = viewModelScope.launch {
        actions.clearCart()
        repository.updateRemoteCart(newCartProducts = listOf<CartItem>())
    }

     fun addOrRemoveFromFavorites(productId: String, productCategory: String) = viewModelScope.launch {
        val newFavoriteProducts =
            actions.toggleItemInFavorites(productId = productId, productCategory = productCategory)
        repository.updateRemoteFavorites(newFavoriteItems = newFavoriteProducts)
    }

     fun removeFromCart(productId: String, productCategory: String) = viewModelScope.launch {
        val newCartProducts =
            actions.removeItemFromCart(productId = productId, productCategory = productCategory)
        repository.updateRemoteCart(newCartProducts = newCartProducts)
    }

     fun updateCartProduct(productId: String, quantity: Int) = viewModelScope.launch {
        val newCartProducts = actions.updateItemFromCart(productId = productId, quantity = quantity)
        repository.updateRemoteCart(newCartProducts = newCartProducts)
    }

    fun order(redirectToLogin: ()->Unit,redirectToAccountData: ()-> Unit) = viewModelScope.launch {
        val email = store.read { it.userData?.email }
        if (email.isNullOrEmpty()) {
            redirectToLogin.invoke()
            return@launch
        }
        val fullAddress = store.read { it.userData }?.let { Utils.getFullAddress(userData = it) }
        if (fullAddress.isNullOrEmpty()) {
            redirectToAccountData.invoke()
            return@launch
        }

        val newOrder = Order(
            userEmail = email,
            orderNumber = UUID.randomUUID(),
            totalPrice = Utils.calculateCartTotalPrice(cartList = cartProducts.value),
            products = cartProducts.value,
            fullAddress = fullAddress
        )
        try {
            async { repository.saveOrder(newOrder = newOrder, email = email) }.await()
            async { actions.saveOrderAndClear(newOrder = newOrder) }.await()
            clearCart()
        } catch (e: Exception) {
            repository.saveErrorToDB(e)
        }
    }

}