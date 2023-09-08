package com.facultate.licenta

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.GoogleSignInStatus
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.OrderStatus
import com.facultate.licenta.model.UserData
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.MappersTo.userData
import com.facultate.licenta.utils.extractCartItem
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
import java.util.UUID
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

    fun updateUserData() = viewModelScope.launch {
        val email = auth.currentUser?.email
        val userData = fetchUserData(email)
        val ordersList = fetchUserOrders(userData?.email)

        val favoriteItems = userData?.favoriteItems ?: emptySet()
        val cartItems = userData?.cartItem ?: emptyList()
        val isAuthenticated = if (auth.currentUser != null) {
            ApplicationState.AuthState.Authenticated
        } else {
            ApplicationState.AuthState.Unauthenticated()
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
    private suspend fun fetchUserOrders(email: String?): MutableList<Order> {
        val ordersList: MutableList<Order> = mutableListOf()
        if (email == null) return ordersList

        val documents = fireStore.collection("Orders").whereEqualTo("userEmail", email).get().await()
        for (doc in documents) {
            ordersList.add(
                Order(
                    userEmail = doc.data["userEmail"] as String,
                    orderNumber = UUID.fromString(doc.data["orderNumber"] as String),
                    totalPrice = doc.data["totalPrice"] as Double,
                    fullAddress = doc.data["fullAddress"] as String,
                    status = when (doc.data["status"] as String) {
                        "Paid" -> OrderStatus.Paid
                        "Shipped" -> OrderStatus.Shipped
                        "Delivered" -> OrderStatus.Delivered
                        else -> OrderStatus.AwaitingPayment
                    },
                    products = extractCartItem(doc.data)  // Assuming `extractCartItem` is a function that converts your document data to a CartItem object
                )
            )
        }
        return ordersList
    }

    private suspend fun fetchUserData(email: String?): UserData? {
        if (email == null) return null

        val document = fireStore.collection("Users").document(email).get().await()
        return if (document.exists()) {
            userData(document.data)
        } else {
            null
        }
    }
}

data class SpecialProduct(
    val productId: String,
    val category: String,
    val discount: Double = 0.0,
)