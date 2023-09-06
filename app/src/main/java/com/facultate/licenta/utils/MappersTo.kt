package com.facultate.licenta.utils

import android.util.Log
import com.facultate.licenta.SpecialProduct
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.CollectionEntry
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.OrderStatus
import com.facultate.licenta.model.Product
import com.facultate.licenta.model.Review
import com.facultate.licenta.model.TagData
import com.facultate.licenta.model.UserData
import com.facultate.licenta.screens.home.calculateRating
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID


object MappersTo {
    //_ to add special product
    fun specialProduct(product: SpecialProduct): HashMap<Any, Any> {
        return hashMapOf(
            "productId" to product.productId,
            "category" to product.category,
            "discount" to product.discount
        )
    }

    fun collectionEntry(document: DocumentSnapshot): CollectionEntry {
        val id = document.getString("id") ?: ""
        val link = document.getString("link") ?: ""

        val dataMap = document.get("data") as? Map<*, *>
        val tagDataMap = dataMap?.get("tagData") as? Map<*, *>
        val tagData = tagDataMap?.let {
            TagData(
                productDescription = it["productDescription"] as? String ?: "",
                productImages = (it["productImages"] as? List<*>)?.mapNotNull { it as? String }
                    ?: emptyList(),
                productName = it["productName"] as? String ?: "",

                productPrice = (it["productPrice"] as String)
                    .replace("$", "")       // Remove the $ sign
                    .replace(",", ".")      // Replace , with .
                    .toDouble()             // Convert to Double
                ,
                id = it["id"] as? String ?: ""
            )
        } ?: TagData("", emptyList(), "", 0.0, "")

        val reviewsList = (document.get("reviews") as? List<*>)?.mapNotNull { reviewMap ->
            val reviewMapTyped = reviewMap as? Map<*, *>
            reviewMapTyped?.let {
                Review(
                    date = it["date"] as? String ?: "",
                    rating = it["rating"].toString().toDouble() / 2,
                    reviewBody = it["reviewBody"] as? String ?: "",
                    title = it["title"] as? String ?: "",
                )
            }
        } ?: emptyList<Review>()

        return CollectionEntry(id, link, tagData, reviewsList)
    }

    fun product(collectionEntry: CollectionEntry, category: String, discount: Double): Product {
        return Product(
            name = collectionEntry.tagData.productName,
            category = category,
            description = collectionEntry.tagData.productDescription,
            images = collectionEntry.tagData.productImages,
            rating = calculateRating(collectionEntry.reviews),
            reviews = collectionEntry.reviews,
            specifications = null,
            price = collectionEntry.tagData.productPrice,
            discount = discount,
            id = collectionEntry.id,
        )
    }

    fun cartItem(
        queryData: CollectionEntry,
        cartItem: CartItemShort,
        discount: Double,
        quantity: Int,
    ): CartItem? {
        var newCartItem1: CartItem?
        newCartItem1 = CartItem(
            productName = queryData.tagData.productName,
            productCategory = cartItem.category,
            productImageDescription = queryData.tagData.productDescription,
            productImage = queryData.tagData.productImages.first(),
            rating = calculateRating(queryData.reviews),
            productPrice = queryData.tagData.productPrice,
            productDiscount = discount,
            productId = cartItem.productId,
            productQuantity = quantity
        )
        return newCartItem1
    }

    fun mapOfCartItem(cartItem: CartItem): Map<String, Any> {
        return mapOf(
            "productName" to cartItem.productName,
            "productCategory" to cartItem.productCategory,
            "productImageDescription" to cartItem.productImageDescription,
            "productImage" to cartItem.productImage,
            "rating" to cartItem.rating,
            "productPrice" to cartItem.productPrice,
            "productDiscount" to cartItem.productDiscount,
            "productId" to cartItem.productId,
            "productQuantity" to cartItem.productQuantity
        )
    }


    fun mapOfUserData(userData: UserData): HashMap<String, Any> {
        return hashMapOf(
            "accountType" to (userData.accountType),
            "firstName" to userData.firstName,
            "lastName" to userData.lastName,
            "email" to userData.email,
            "phoneNumber" to userData.phoneNumber,
            "address" to userData.address,
            "zipCode" to userData.zipCode,
            "city" to userData.city,
            "state" to userData.state,
            "favoriteItems" to userData.favoriteItems.toList(),
            "cartItems" to userData.cartItem.toList(),
            "orders" to userData.orders
        )
    }

    fun userData(hashMap: MutableMap<String, Any>?): UserData? {
        if (hashMap == null) {
            return null
        }
        return UserData(
            accountType = hashMap["accountType"].toString(),
            firstName = hashMap["firstName"].toString(),
            lastName = hashMap["lastName"].toString(),
            email = hashMap["email"].toString(),
            phoneNumber = hashMap["phoneNumber"].toString(),
            address = hashMap["address"].toString(),
            zipCode = hashMap["zipCode"].toString(),
            city = hashMap["city"].toString(),
            state = hashMap["state"].toString(),
            favoriteItems = extractFavoriteItem(
                hashMap = hashMap
            ),
            cartItem = extractCartItem(hashMap = hashMap),
        )
    }

    fun favoriteItems(favoriteItems: Set<Pair<String, String>>): MutableSet<FavoriteItem> {
        val toReturn = mutableSetOf<FavoriteItem>()
        favoriteItems.forEach { item ->
            toReturn.add(
                FavoriteItem(
                    productId = item.second,
                    category = item.first
                )
            )
        }
        return toReturn
    }
}

fun extractFavoriteItem(hashMap: MutableMap<String, Any>?): Set<FavoriteItem> {
    if (hashMap == null) {
        return emptySet()
    } else {
        val items = hashMap["favoriteItems"]
        return if (items is List<*>) {
            items.filterIsInstance<Map<String, String>>()
                .map { entry ->
                    FavoriteItem(
                        productId = entry["productId"] ?: "",
                        category = entry["category"] ?: ""
                    )
                }
                .toSet()
        } else {
            emptySet()
        }
    }
}

fun extractCartItem(hashMap: MutableMap<String, Any>?): List<CartItem> {
    if (hashMap == null) {
        return emptyList()
    } else {
        val items = hashMap["products"]
        return if (items is List<*>) {
            items.filterIsInstance<Map<String, Any>>() // Changed to Any since some fields might not be String
                .map { entry ->
                    CartItem(
                        productId = entry["productId"] as? String ?: "",
                        productName = entry["productName"] as? String ?: "",
                        productImage = entry["productImage"] as? String ?: "",
                        productImageDescription = entry["productImageDescription"] as? String ?: "",
                        productPrice = (entry["productPrice"] as? Number)?.toDouble() ?: 0.0,
                        productDiscount = (entry["productDiscount"] as? Number)?.toDouble() ?: 0.0,
                        productCategory = entry["productCategory"] as? String ?: "",
                        productQuantity = (entry["productQuantity"] as? Number)?.toInt() ?: 1,
                        rating = (entry["rating"] as? Number)?.toDouble() ?: 0.0
                    )
                }
                .toList()
        } else {
            emptyList()
        }
    }
}

fun mapOrderToFirebaseData(order: Order): MutableMap<String, Any> {
    val orderData = mutableMapOf<String, Any>()

    orderData["userEmail"] = order.userEmail
    orderData["orderNumber"] = order.orderNumber.toString()
    orderData["totalPrice"] = order.totalPrice
    orderData["fullAddress"] = order.fullAddress
    orderData["status"] = when (order.status) {
        is OrderStatus.Paid -> "Paid"
        is OrderStatus.Shipped -> "Shipped"
        is OrderStatus.Delivered -> "Delivered"
        else -> "AwaitingPayment"
    }

    val productsData = order.products.map { product ->
        mutableMapOf(
            "productId" to product.productId,
            "productName" to product.productName,
            "productImage" to product.productImage,
            "productImageDescription" to product.productImageDescription,
            "productPrice" to product.productPrice,
            "productDiscount" to product.productDiscount,
            "productCategory" to product.productCategory,
            "productQuantity" to product.productQuantity,
            "rating" to product.rating
        )
    }
    orderData["products"] = productsData

    return orderData
}












