package com.facultate.licenta.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.Product
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.MappersTo
import com.facultate.licenta.utils.MappersTo.product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ProductPageViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    val repository: Repository,
    private val actions: Actions,
) : ViewModel() {
    val fireStore = Firebase.firestore

    var product = MutableStateFlow<DataState<Product>>(DataState.Loading)
    val isFavorite = MutableStateFlow(false)
    val recommendations = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)

     fun updateProduct(productCategory: String, productId: String) = viewModelScope.launch {
        product.value =
            repository.getProduct(productCategory = productCategory, productId = productId)
        isFavorite.value = actions.checkIfProductIsFavorite(productId = productId)
    }

     fun getRecommendedProducts() = viewModelScope.launch {
        recommendations.value = repository.getRecommendedProducts()
    }

     fun toggleFavorite(productId: String, productCategory: String) = viewModelScope.launch {
        val newFavoriteItems = actions.toggleItemInFavorites(productId, productCategory)
        isFavorite.value = newFavoriteItems.contains(
            FavoriteItem(
                productId = productId, category = productCategory
            )
        )
        repository.updateRemoteFavorites(newFavoriteItems)
    }

     fun addToCart(
        productId: String, productCategory: String, discount: Double, quantity: Int,
    ) = viewModelScope.launch {
        val newCartItems = actions.getCartItems().toMutableList()
        val item = repository.getCartItem(
            cartItem = CartItemShort(
                productId = productId, category = productCategory
            ), discount = discount, quantity = quantity
        )
        val existingItem = newCartItems.find { it.productId == item.productId }

        if (existingItem != null) {
            val index = newCartItems.indexOf(existingItem)
            newCartItems[index] =
                existingItem.copy(productQuantity = existingItem.productQuantity + quantity)
        } else {
            newCartItems.add(item)
        }
        actions.updateCart(newCartProducts = newCartItems.toList<CartItem>())
        repository.updateRemoteCart(newCartProducts = newCartItems.toList<CartItem>())
    }
}
