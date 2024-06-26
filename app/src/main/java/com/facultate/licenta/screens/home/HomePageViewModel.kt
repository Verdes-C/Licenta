package com.facultate.licenta.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.Product
import com.facultate.licenta.model.Review
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val repository: FirebaseRepository,
    private val actions: Actions,
) : ViewModel() {
    val promotions = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val newArrivals = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val findNew = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)

    fun loadHomeData() = viewModelScope.launch {
        val promotionsDeferred = async { fetchProducts("Promotions") }
        val newArrivalsDeferred = async { fetchProducts("New Arrivals") }
        val findNewDeferred = async { fetchProducts("Find Something New") }

        promotions.value = promotionsDeferred.await()
        newArrivals.value = newArrivalsDeferred.await()
        findNew.value = findNewDeferred.await()
    }

    suspend fun addToCart(cartItem: CartItemShort, discount: Double = 0.0, quantity: Int = 1) =
        coroutineScope {
            val newCartItems = actions.getCartItems().toMutableList()
            val item = repository.getCartItem(
                cartItem = cartItem,
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

    private suspend fun fetchProducts(collection: String): DataState<List<Product>> {
        return try {
            DataState.Success(repository.getSpecialProducts(collection = collection))
        } catch (e: Exception) {
            DataState.Error(e)
        }
    }
}

fun calculateRating(reviews: List<Review>): Double {
    return Utils.calculateRating(reviews = reviews)
}