package com.facultate.licenta

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.MappersTo.userData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
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
                cartItems = userData?.cartItem!!
                isAuthenticated = ApplicationState.AuthState.Authenticated
            } else {
                emptySet<FavoriteItem>() // Or handle the case where userData is null
            }

            store.update { applicationState ->
                Log.d("TESTING", "update -> ${userData.toString()}")
                applicationState.copy(
                    authState = isAuthenticated,
                    userData = userData,
                    favoriteItems = favoriteItems.toMutableSet(),
                    cartProducts = cartItems
                )
            }
        }
    }



//    fun addToCart(productId: String, quantity:Int = 1){
//        viewModelScope.launch{
//            store.update {applicationState ->
//                val cartItems = applicationState.cartProducts as MutableList
//                var itemNotInCart = false
//                cartItems.forEach { item->
//                    if (productId == item.productId){
//                        item.productQuantity += quantity
//                        itemNotInCart = true
//                    }
//                }
//                if(itemNotInCart){
//                    cartItems + CartItem(productId = productId, productQuantity = quantity)
//                }
//                return@update applicationState.copy(
//                    cartProducts = cartItems
//                )
//            }
//        }
//    }

}

data class SpecialProduct(
    val productId: String,
    val category: String,
    val discount: Double = 0.0,
)