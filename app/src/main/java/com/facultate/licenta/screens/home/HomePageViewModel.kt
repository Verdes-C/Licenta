package com.facultate.licenta.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.hilt.interfaces.ProductRepository
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.product.Product
import com.facultate.licenta.utils.CartItemShort
import com.facultate.licenta.utils.FavoriteItem
import com.facultate.licenta.utils.Review
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class HomePageViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val repository: ProductRepository,
    private val actions: Actions
) : ViewModel() {
    val promotions = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val newArrivals = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)
    val findNew = MutableStateFlow<DataState<List<Product>>>(DataState.Loading)

    fun loadHomeData() {
        viewModelScope.launch {
            val promotionsDeferred = async { fetchProducts("Promotions") }
            val newArrivalsDeferred = async { fetchProducts("New Arrivals") }
            val findNewDeferred = async { fetchProducts("Find Something New") }

            promotions.value = promotionsDeferred.await()
            newArrivals.value = newArrivalsDeferred.await()
            findNew.value = findNewDeferred.await()
        }
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
    val totalRating = reviews.sumOf { it.rating / 2 }
    return (totalRating / reviews.size).let { String.format("%.2f", it).toDouble() }
}


sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
}



