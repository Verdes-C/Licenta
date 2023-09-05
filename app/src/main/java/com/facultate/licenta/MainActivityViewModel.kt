package com.facultate.licenta

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.GoogleSignInStatus
import com.facultate.licenta.model.Order
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.MappersTo.userData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val store: Store<ApplicationState>,
) : ViewModel() {
    private val auth = Firebase.auth
    private val fireStore = Firebase.firestore

    fun addOrUpdateToHomeSection(products: List<SpecialProduct>, section: String) {
        products.forEach { product ->
            fireStore.collection(section)
                .document(product.productId)
                .set(product)
                .addOnSuccessListener { ref ->
                    Log.d("TESTING", "DocumentSnapshot added with ID: $ref")
                }
                .addOnFailureListener { e ->
                    Log.w("TESTING", e.toString(), e)
                }
        }
    }

    fun updateUserData() {
        var favoriteItems: Set<FavoriteItem> = setOf<FavoriteItem>()
        var cartItems: List<CartItem> = listOf<CartItem>()
        var isAuthenticated: ApplicationState.AuthState = ApplicationState.AuthState.Unauthenticated()
        var ordersList: List<Order> = listOf()
        viewModelScope.launch {
            val email = auth.currentUser?.email
            val userData = if (email != null) {
                val documentSnapshot = fireStore.collection("Users")
                    .document(email)
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    userData(documentSnapshot.data)
                } else {
                    null
                }
            } else {
                null
            }

            if (auth.currentUser != null) {
                favoriteItems = userData?.favoriteItems!!
                cartItems = userData.cartItem
                isAuthenticated = ApplicationState.AuthState.Authenticated
                ordersList = userData.orders
            } else {
                emptySet<FavoriteItem>() // Or handle the case where userData is null
            }

            store.update { applicationState ->
                applicationState.copy(
                    authState = isAuthenticated,
                    userData = userData,
                    favoriteItems = favoriteItems.toMutableSet(),
                    cartProducts = cartItems,
                    orders = ordersList
                )
            }
        }
    }
}

data class SpecialProduct(
    val productId: String,
    val category: String,
    val discount: Double = 0.0,
)