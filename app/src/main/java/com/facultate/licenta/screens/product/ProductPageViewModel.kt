package com.facultate.licenta.screens.product

import androidx.lifecycle.ViewModel
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class ProductPageViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    val repository: Repository,
    private val actions: Actions,
) : ViewModel() {
    val fireStore = Firebase.firestore

    var product = MutableStateFlow<Product?>(null)
    val isFavorite = MutableStateFlow(false)
    val recommendations = MutableStateFlow<List<Product?>>(listOf())

    suspend fun updateProduct(productCategory: String, productId: String) = coroutineScope {
        // Start the first query
        val queryResult1Deferred = async {
            fireStore.collection(productCategory).whereEqualTo("id", productId).get().await()
        }

        // Start the second query
        val discountQueryDeferred = async {
            fireStore.collection("Promotions").whereEqualTo("productId", productId).get().await()
        }

        // Await the results of both queries
        val queryResult1 = queryResult1Deferred.await()
        val discountQuery = discountQueryDeferred.await()

        if (queryResult1.documents.isNotEmpty()) {
            val result = MappersTo.collectionEntry(queryResult1.documents.first())
            var discount = 0.0
            if (discountQuery.documents.isNotEmpty()) {
                val data = discountQuery.documents.first().data
                discount = data?.get("discount").toString().toDouble()
            }
            product.value =
                product(collectionEntry = result, category = productCategory, discount = discount)
            store.read { applicationState ->
                if (applicationState.favoriteItems.any { it.productId == productId }) {
                    isFavorite.value = true
                }
            }
        }
    }
    suspend fun getRecommendedProducts() = coroutineScope {
        val recommendationsQueryDeferred = async {
            fireStore.collection("Find Something New").get().await()
        }
        val recommendationsQuery = recommendationsQueryDeferred.await()

        if (recommendationsQuery.documents.isNotEmpty()) {
            val result = recommendationsQuery.map { queryResult ->
                val category = queryResult.data["category"] as String
                val productId = queryResult.data["productId"] as String
                val document = async {
                    fireStore.collection(category).whereEqualTo("id", productId).get().await()
                }.await().documents.first()
                val collection = MappersTo.collectionEntry(document)
                val product = product(
                    collectionEntry = collection,
                    category = queryResult.data["category"] as String,
                    discount = queryResult.data["discount"] as Double
                )
                return@map product
            }
            recommendations.value = result

        }
    }
    suspend fun toggleFavorite(productId: String, productCategory: String) {
        val newFavoriteItems = actions.toggleItemInFavorites(productId, productCategory)
        isFavorite.value = newFavoriteItems.contains(
            FavoriteItem(
                productId = productId, category = productCategory
            )
        )
        repository.updateRemoteFavorites(newFavoriteItems)
    }
    suspend fun addToCart(
        productId: String, productCategory: String, discount: Double, quantity: Int
    ) {
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
                existingItem.copy(productQuantity = existingItem.productQuantity + 1)
        } else {
            newCartItems.add(item)
        }
        actions.updateCart(newCartProducts = newCartItems.toList<CartItem>())
        repository.updateRemoteCart(newCartProducts = newCartItems.toList<CartItem>())
    }
}
