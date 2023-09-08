package com.facultate.licenta.firebase

import android.content.Context
import android.widget.Toast
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.OrderStatus
import com.facultate.licenta.model.Product
import com.facultate.licenta.model.UserData
import com.facultate.licenta.screens.home.calculateRating
import com.facultate.licenta.utils.MappersTo
import com.facultate.licenta.utils.MappersTo.cartItem
import com.facultate.licenta.utils.MappersTo.collectionEntry
import com.facultate.licenta.utils.MappersTo.product
import com.facultate.licenta.utils.mapOrderToFirebaseData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class Repository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : FirebaseRepository {
    override suspend fun signUpUsingEmailAndPassword(
        viewModelScope: CoroutineScope,
        email: String,
        password: String,
    ): String = coroutineScope {
        var message = ""
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        viewModelScope.launch {
                            updateUserData(userData = UserData(email = email))
                            message = try {
                                auth.currentUser?.sendEmailVerification()
                                "Not verified"
                            } catch (error: Exception) {
                                error.message!!
                            }
                        }
                    }
                }.addOnFailureListener { exception ->
                    message = exception.message!!
                }.await()
        } catch (e: Exception) {
            message = e.message!!
        }
        return@coroutineScope message
    }

    override suspend fun retrieveUserData(
        email: String,
    ): UserData? = coroutineScope {
        var userData: UserData? = null

        val document = firestore.collection("Users").document(email).get().await()
        if (document.exists()) {
            userData = MappersTo.userData(document.data)
        }
        return@coroutineScope userData
    }

    override suspend fun updateUserData(
        userData: UserData,
    ) {
        firestore.collection("Users")
            .document(userData.email)
            .set(
                MappersTo.mapOfUserData(
                    userData = userData
                ),
                SetOptions.merge()  //_ Create or merge data
            )
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
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
                                collectionEntry(specificDocumentSnapshot.documents.first())
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
                                    discount = promotionDocument.data["discount"].toString()
                                        .toDouble(),
                                    id = id
                                )
                            )
                        }
                    }
                    promotionTasks.add(task)
                }
            }
            promotionTasks.awaitAll()
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        return@coroutineScope results.toList()
    }

    override suspend fun getProduct(
        productCategory: String,
        productId: String,
    ): DataState<Product> =
        coroutineScope {
            try {
                val promotionsQuery =
                    firestore.collection("Promotions").whereEqualTo("productId", productId).get()
                val deferredProduct = async {
                    firestore.collection(productCategory).whereEqualTo("id", productId).get()
                        .await().documents.mapNotNull {
                            val promotionsIds = promotionsQuery.await().documents.map {
                                it.data?.get("productId") as String
                            }
                            val discount = if (productId in promotionsIds) {
                                promotionsQuery.await().documents.mapNotNull {
                                    if (it.data?.get("productId") as String == productId) {
                                        it.data!!.get("discount") as Double
                                    } else {
                                        null
                                    }
                                }.firstOrNull() ?: 0.0
                            } else {
                                0.0
                            }
                            product(
                                collectionEntry = collectionEntry(it),
                                category = productCategory,
                                discount = discount
                            )
                        }.first()
                }
                return@coroutineScope DataState.Success(deferredProduct.await())
            } catch (e: Exception) {
                saveErrorToDB(e)
                return@coroutineScope DataState.Error(e)
            }
        }

    override suspend fun getRecommendedProducts(category: String): DataState<List<Product>> =
        coroutineScope {
            try {
                val recommendedQuery = firestore.collection(category).get()
                val results = async {
                    return@async recommendedQuery.await().documents.mapNotNull { document ->
                        val productCategory = document.data?.get("category") as String
                        val productId = document.data?.get("productId") as String
                        val discount = document.data?.get("discount") as Double
                        firestore.collection(productCategory).whereEqualTo("id", productId).get()
                            .await().documents.map {
                                product(
                                    collectionEntry = collectionEntry(it),
                                    category = productCategory,
                                    discount = discount
                                )
                            }.firstOrNull()
                    }
                }
                val toBeReturned = results.await()
                return@coroutineScope DataState.Success(toBeReturned)
            } catch (e: Exception) {
                saveErrorToDB(e)
                return@coroutineScope DataState.Error(e)
            }
        }

    override suspend fun getSearchProducts(
        category: String?,
        searchInput: String?,
    ): DataState<List<Product>> =
        coroutineScope {
            val collectionList = listOf(
                "Art Brush",
                "Calligraphy Brush",
                "Calligraphy Dip Pen",
                "Calligraphy Fountain Pen",
                "Calligraphy Nibs",
                "Cardridges",
                "Flexible Nib",
                "Ink",
                "Italic & Stub Nib",
                "Luxury Fountain Pen",
            )
            val specialList = listOf(
                "Promotions",
                "Find Something New",
                "New Arrivals"
            )
            val checks = searchInput?.lowercase()?.split(" ")
            val resultItems: MutableList<Product> = mutableListOf()
            //_ Get promotions Ids to check
            try {
                val promotions = firestore.collection("Promotions").get()
                val promotionsIds = promotions.await().documents.mapNotNull { document ->
                    if (document.exists()) {
                        return@mapNotNull document.data?.get("productId")
                    } else {
                        null
                    }
                }
                //_ Search by input
                if (category == null) {
                    val deferredResults = collectionList.map { collection ->
                        async {
                            val documents = firestore.collection(collection).get()
                            val docResults = documents.await().documents.filter { document ->
                                checks?.any { check ->
                                    val docData = document.data?.get("data") as HashMap<String, Any>
                                    val tagData = docData.get("tagData") as HashMap<String, Any>
                                    val productName = tagData["productName"] as String
                                    check in (productName.lowercase().split(" "))
                                } ?: false
                            }
                            val products = docResults.mapNotNull { document ->
                                val discount =
                                    if (document.data?.get("id") as String in promotionsIds) {
                                        promotions.await().documents.mapNotNull {
                                            if (it.data?.get("productId") as String == document.data!!["id"] as String) {
                                                it.data!!["discount"] as Double
                                            } else {
                                                null
                                            }
                                        }.firstOrNull() ?: 0.0
                                    } else {
                                        0.0
                                    }
                                product(
                                    collectionEntry = collectionEntry(document),
                                    category = collection,
                                    discount = discount
                                )
                            }
                            products
                        }
                    }
                    val allProducts = deferredResults.awaitAll().flatten()
                    resultItems.addAll(allProducts)
                }
                //_ Search based on category
                else {
                    if (category in specialList) {
                        return@coroutineScope getRecommendedProducts(category = category)
                    }
                    val deferredResults = firestore.collection(category).get()
                        .await().documents.mapNotNull { document ->
                            async {
                                val discount =
                                    if (document.data?.get("id") as String in promotionsIds) {
                                        promotions.await().documents.mapNotNull {
                                            if (it.data?.get("productId") as String == document.data!!["id"] as String) {
                                                it.data!!["discount"] as Double
                                            } else {
                                                null
                                            }
                                        }.firstOrNull() ?: 0.0
                                    } else {
                                        0.0
                                    }
                                product(
                                    collectionEntry = collectionEntry(document),
                                    category = category,
                                    discount = discount
                                )
                            }
                        }
                    val allProducts = deferredResults.awaitAll()
                    resultItems.addAll(allProducts)
                }
                return@coroutineScope DataState.Success(resultItems.toList())
            } catch (e: Exception) {
                return@coroutineScope DataState.Error(e)
            }
        }

    override suspend fun getCartItem(
        cartItem: CartItemShort,
        discount: Double,
        quantity: Int,
    ): CartItem =
        coroutineScope {
            var newCartItem: CartItem? = null
            try {
                val documentQuery =
                    firestore.collection(cartItem.category).document(cartItem.productId).get()
                val queryData = collectionEntry(documentQuery.await())
                newCartItem = cartItem(
                    queryData = queryData,
                    cartItem = cartItem,
                    discount = discount,
                    quantity = quantity
                )
            } catch (e: Exception) {
                saveErrorToDB(e)
            }
            return@coroutineScope newCartItem!!
        }

    override suspend fun updateRemoteCart(newCartProducts: List<CartItem>) {
        val userEmail = auth.currentUser?.email
        val userQuery = userEmail?.let { email ->
            firestore.collection("Users").document(email).get()
        }
        try {
            val document = userQuery?.await()
            document?.reference?.update("cartItems", newCartProducts)?.await()
        } catch (e: Exception) {
            saveErrorToDB(e)
        }
    }


    override suspend fun getFavoriteItems(
        favoriteItems: Set<FavoriteItem>,
    ): List<Product> = coroutineScope {
        val deferredList = favoriteItems.map { item: FavoriteItem ->
            async {
                var discount = 0.0
                val documentSnapshot = firestore.collection("Promotions")
                    .document(item.productId)
                    .get()
                    .await()
                if (documentSnapshot.exists()) {
                    discount = documentSnapshot.get("discount") as Double
                }
                val collectionDocument = firestore.collection(item.category)
                    .document(item.productId)
                    .get()
                    .await()
                val entry = collectionEntry(
                    collectionDocument
                )
                return@async product(entry, item.category, discount)
            }
        }
        return@coroutineScope deferredList.awaitAll()
    }

    override suspend fun updateRemoteFavorites(newFavoriteItems: Set<FavoriteItem>) {
        val userEmail = auth.currentUser?.email
        val userDocumentReference = userEmail?.let { email ->
            firestore.collection("Users").document(email).get()
        }

        try {
            val document = userDocumentReference?.await()
            document?.reference?.update("favoriteItems", newFavoriteItems.toList())?.await()
        } catch (e: Exception) {
            saveErrorToDB(e)
        }
    }

    override suspend fun saveOrder(newOrder: Order, email: String) {
        val orderData = mapOrderToFirebaseData(newOrder)

        val document =
            firestore.collection("Orders").document(newOrder.orderNumber.toString()).get().await()
        document.reference.set(orderData).addOnSuccessListener {
            firestore.collection("Users").document(newOrder.userEmail).get()
                .addOnSuccessListener { document ->
                    val orders = (document.data?.get("orders") as List<String>).toMutableList()
                    orders.add(newOrder.orderNumber.toString())
                    document.reference.update("orders", orders)
                }
        }
    }

    override suspend fun getUnfulfilledOrders(): List<Order> = coroutineScope {
        try {
            val documents =
                firestore.collection("Orders").whereNotEqualTo("status", "Delivered").get().await()
                    .mapNotNull { document ->
                        if (document.exists()) async {
                            return@async Order(
                                userEmail = document["userEmail"] as String,
                                orderNumber = UUID.fromString(document["orderNumber"] as String),
                                totalPrice = document["totalPrice"] as Double,
                                fullAddress = document["fullAddress"] as String,
                                status = when (document["status"] as String) {
                                    "Paid" -> OrderStatus.Paid
                                    "Shipped" -> OrderStatus.Shipped
                                    "Delivered" -> OrderStatus.Delivered
                                    else -> OrderStatus.AwaitingPayment
                                },
                                products = (document["products"] as List<HashMap<String, Any>>).map { product ->
                                    CartItem(
                                        productId = product["productId"] as? String ?: "",
                                        productName = product["productName"] as? String ?: "",
                                        productImage = product["productImage"] as? String ?: "",
                                        productImageDescription = product["productImageDescription"] as? String
                                            ?: "",
                                        productPrice = (product["productPrice"] as? Number)?.toDouble()
                                            ?: 0.0,
                                        productDiscount = (product["productDiscount"] as? Number)?.toDouble()
                                            ?: 0.0,
                                        productCategory = product["productCategory"] as? String ?: "",
                                        productQuantity = (product["productQuantity"] as? Number)?.toInt()
                                            ?: 1,
                                        rating = (product["rating"] as? Number)?.toDouble() ?: 0.0
                                    )
                                }
                            )
                        } else null
                    }
            return@coroutineScope documents.awaitAll()
        }catch (e:Exception){
            saveErrorToDB(e)
            return@coroutineScope listOf()
        }
    }

    override suspend fun updateOrder(updatedOrder: Order) {
        val document =
            firestore.collection("Orders").document(updatedOrder.orderNumber.toString()).get()
                .await()
        try {
            document.reference.update(mapOrderToFirebaseData(order = updatedOrder)).await()
        }catch (e:Exception){
            saveErrorToDB(e)
        }
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
        //_Firebase Crashlytics
        FirebaseCrashlytics.getInstance().recordException(exception)
    }

    override fun notifyUserOfError(context: Context, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}

