package com.facultate.licenta.firebase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.facultate.licenta.MainActivity
import com.facultate.licenta.R
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.Product
import com.facultate.licenta.model.UserData
import com.facultate.licenta.screens.home.calculateRating
import com.facultate.licenta.utils.MappersTo
import com.facultate.licenta.utils.MappersTo.cartItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.rpc.context.AttributeContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.concurrent.Flow
import javax.inject.Inject

class Repository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : FirebaseRepository {
    override suspend fun signUpUsingEmailAndPassword(
        viewModelScope: CoroutineScope,
        email: String,
        password: String
    ): String {
        var message: String = ""
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        viewModelScope.launch {
                            updateUserData(userData = UserData(email = email))
                            try {
                                auth.currentUser?.sendEmailVerification()
                                message = "Not verified"
                            } catch (error: Exception) {
                                message = error.message!!
                            }
                        }
                    }
                }.addOnFailureListener { exception ->
                    message = exception.message!!
                }.await()
        } catch (e: Exception) {
            message = e.message!!
        }
        return message
    }

    override suspend fun retrieveUserData(
        email: String,
    ): UserData? = coroutineScope {
        var userData: UserData? = null

        val document = firestore.collection("Users").document(email).get().await()
        Log.d("GOOGLE", "inside of retrieveUserData ${document}")
        Log.d("GOOGLE", "inside of retrieveUserData check for document.doc ${document.data}")
        if(document.exists()){
            Log.d("GOOGLE", "inside of retrieveUserData - document exists ${document}")
            userData = MappersTo.userData(document.data)
        }
        return@coroutineScope userData
    }

    override suspend fun updateUserData(
        userData: UserData
    ) {
        Log.d("GOOGLE", "inside of updateuserData ${userData}")
        firestore.collection("Users")
            .document(userData.email)
            .set(
                MappersTo.mapOfUserData(
                    userData = userData
                ),
                SetOptions.merge()  //_ Create or merge data
            )
            .addOnSuccessListener {
                Log.d("GOOGLE", "saved successfully ${userData}")

            }
            .addOnFailureListener { exception ->
                Log.d("GOOGLE", "failed ${userData} and error ${exception.message}")

                saveErrorToDB(exception)
            }
            .await()
    }

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


                    newCartItem = cartItem(queryData, cartItem, discount, quantity)

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
            Log.w("TESTING", "Error updating cart items: ", e)
        }
    }

    override fun saveErrorToDB(exception: java.lang.Exception) {
        val errorReport = hashMapOf(
            "date" to LocalDateTime.now().toString(),
            "error" to exception.message,
            "cause" to exception.cause?.toString(),
            "stackTrace" to exception.stackTrace.joinToString("\n"),
            "localizedMessage" to exception.localizedMessage,
        )
        firestore.collection("errors")
            .add(errorReport)
    }

    override fun notifyUserOfError(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override suspend fun resetPassword(email: String): String {
        return try {
            auth.sendPasswordResetEmail(email).await()
            "An email with the password reset steps was sent to $email"
        } catch (e: Exception) {
            when (e) {
                is FirebaseAuthInvalidUserException -> {
                    "No user found with the provided email address."
                }

                else -> {
                    "An error occurred: ${e.message}"
                }
            }
        }
    }

    override suspend fun getFavoriteItems(
        viewModelScope: CoroutineScope,
        favoriteItems: Set<FavoriteItem>
    ): List<Product> {
        val deferredList = favoriteItems.map { item: FavoriteItem ->
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
                return@async MappersTo.product(entry, item.category, discount)
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
