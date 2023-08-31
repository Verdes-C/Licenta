package com.facultate.licenta.firebase

import android.util.Log
import com.facultate.licenta.hilt.interfaces.ProductRepository
import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.home.calculateRating
import com.facultate.licenta.screens.product.Product
import com.facultate.licenta.utils.CartItemShort
import com.facultate.licenta.utils.FavoriteItem
import com.facultate.licenta.utils.MappersTo
import com.facultate.licenta.utils.MappersTo.cartItem
import com.facultate.licenta.utils.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseProductRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : ProductRepository {
    override suspend fun signUpUsingEmailAndPassword(
        viewModelScope: CoroutineScope,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    viewModelScope.launch {
                        logInWithEmailAndPassword(
                            viewModelScope = viewModelScope,
                            email = email,
                            password = password
                        )
                    }
                }
            }.await()

            firestore.collection("Users")
                .document(email)
                .set(
                    MappersTo.mapOfUserData(
                        userData = UserData(
                            email = email
                        )
                    ),
                    SetOptions.merge()  //_ Create or merge data
                )
                .addOnSuccessListener {
                    Log.d("TESTING", "saved")
                }
                .await()
        }
    }

    override suspend fun logInWithEmailAndPassword(
        viewModelScope: CoroutineScope,
        email: String,
        password: String
    ): UserData? = coroutineScope {
        var userData: UserData? = null

        val document = firestore.collection("Users").document(email).get().await()
        userData = MappersTo.userData(document.data)

        return@coroutineScope userData
    }

    /**
     * Fetches special products based on the given collection.
     *
     * @param collection The name of the collection to fetch products from.
     * @return A list of products from the specified collection.
     */
    override suspend fun getSpecialProducts(collection: String): List<Product> = coroutineScope {
        val results = mutableListOf<Product>()

        try {
            val promotionsResult = firestore.collection(collection).get().await()
            val promotionTasks = mutableListOf<Deferred<Unit>>()

            for (promotionDocument in promotionsResult) {
                val category = promotionDocument.getString("category")
                val id = promotionDocument.getString("productId")

                if (category != null && id != null) {
                    val task = async {
                        val specificDocumentSnapshot =
                            firestore.collection(category)
                                .whereEqualTo("id", id)
                                .get()
                                .await()

                        if (specificDocumentSnapshot.size() > 0) {
                            val queryData =
                                MappersTo.collectionEntry(specificDocumentSnapshot.documents.first())
                            results.add(
                                Product(
                                    name = queryData.tagData.productName,
                                    category = category,
                                    description = queryData.tagData.productDescription,
                                    images = queryData.tagData.productImages,
                                    rating = calculateRating(queryData.reviews),
                                    reviews = queryData.reviews,
                                    specifications = null,
                                    price = queryData.tagData.productPrice,
                                    discount = promotionDocument.data.get("discount").toString()
                                        .toDouble(),
                                    id = id
                                )
                            )
                        }
                    }
                    promotionTasks.add(task)
                }
            }

            // Wait for all tasks to complete
            promotionTasks.awaitAll()
        } catch (e: Exception) {
            Log.w("TAG", "Error getting products", e)
        }

        results.toList() // Convert to immutable list before returning


        return@coroutineScope results.toList()
    }

    override suspend fun getCartItem(
        cartItem: CartItemShort,
        discount: Double,
        quantity: Int
    ): CartItem =
        coroutineScope {
            var newCartItem: CartItem? = null

            try {
                val documentResult =
                    firestore.collection(cartItem.category).document(cartItem.productId).get()
                        .await()
                if (documentResult.exists()) {
                    val queryData =
                        MappersTo.collectionEntry(documentResult)


                    newCartItem = cartItem(newCartItem, queryData, cartItem, discount, quantity)

                }
            } catch (e: Exception) {

                Log.w("TAG", "Error getting products", e)
            }
            return@coroutineScope newCartItem!!
        }

    override suspend fun updateRemoteCart(newCartProducts: List<CartItem>) {
        val userEmail = auth.currentUser?.email
        val userDocumentReference = userEmail?.let { email ->
            firestore.collection("Users").document(email)
        }
        try {
            val document = userDocumentReference?.get()?.await()
            document?.reference?.update("cartItems", newCartProducts)?.await()
            println("Updated cartItems: $newCartProducts")
        } catch (e: Exception) {
            Log.w("TESTING", "Error updating favorite items: ", e)
        }
    }

    override suspend fun getFavoriteItems(
        viewModelScope: CoroutineScope,
        favoriteItems: Set<FavoriteItem>
    ): List<Product> {
        val deferredList = favoriteItems.map { item ->
            viewModelScope.async {
                var discount: Double = 0.0
                val documentSnapshot = firestore.collection("Promotions")
                    .document(item.productId)
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    discount = documentSnapshot.getDouble("discount") ?: 0.0
                }

                val collectionDocument = firestore.collection(item.category)
                    .document(item.productId)
                    .get()
                    .await()
                val entry = MappersTo.collectionEntry(
                    collectionDocument
                )
                MappersTo.product(entry, item.category, discount)
            }
        }

        return deferredList.awaitAll()
    }

    override suspend fun updateRemoteFavorites(newFavoriteItems: Set<FavoriteItem>) {
        val userEmail = auth.currentUser?.email
        val userDocumentReference = userEmail?.let { email ->
            firestore.collection("Users").document(email)
        }

        try {
            val document = userDocumentReference?.get()?.await()
            document?.reference?.update("favoriteItems", newFavoriteItems.toList())?.await()
            println("Updated favoriteItems: $newFavoriteItems")
        } catch (e: Exception) {
            Log.w("TESTING", "Error updating favorite items: ", e)
        }
    }


}
