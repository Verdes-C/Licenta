package com.facultate.licenta.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.Product
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewmodel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: Repository,
) : ViewModel() {

    //_ Due to the fact that we need List<Product> to display
    var favoriteItems = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)

    fun getFavoriteItems() = viewModelScope.launch {
        val favoriteItemsRead = actions.getFavoriteItems()

        try {
            val result = repository.getFavoriteItems(
                favoriteItems = favoriteItemsRead
            )
            favoriteItems.value = DataState.Success(result)
        } catch (e: Exception) {
            favoriteItems.value = DataState.Error(e)
        }
    }

     fun removeFromFavorite(productId: String, productCategory: String) = viewModelScope.launch{
        val newFavoriteItems = actions.toggleItemInFavorites(
            productId = productId,
            productCategory = productCategory
        )
        try {
            repository.updateRemoteFavorites(newFavoriteItems = newFavoriteItems)
            val result = repository.getFavoriteItems(favoriteItems = newFavoriteItems)
            favoriteItems.value = DataState.Success(result)
        } catch (e: Exception) {
            favoriteItems.value = DataState.Error(e)
        }
    }

     fun addToCart(
        productId: String,
        productCategory: String,
        discount: Double,
        quantity: Int,
    ) = viewModelScope.launch {
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