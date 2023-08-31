package com.facultate.licenta.utils

import com.facultate.licenta.SpecialProduct
import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.home.calculateRating
import com.facultate.licenta.screens.product.Product
import com.google.firebase.firestore.DocumentSnapshot


object MappersTo {
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
                productPrice = (it["productPrice"] as String).replace(Regex("[^\\d.]"), "")
                    .toDouble(),
                id = it["id"] as? String ?: ""
            )
        } ?: TagData("", emptyList(), "", 0.0, "")

        val reviewsList = (document.get("reviews") as? List<*>)?.mapNotNull { reviewMap ->
            val reviewMapTyped = reviewMap as? Map<*, *>
            reviewMapTyped?.let {
                Review(
                    date = it["date"] as? String ?: "",
                    rating = it["rating"].toString().toDouble(),
                    reviewBody = it["reviewBody"] as? String ?: "",
                    title = it["title"] as? String ?: "",
                )
            }
        } ?: emptyList()

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
            id = collectionEntry.id
        )
    }

    fun cartItem(
        newCartItem: CartItem?,
        queryData: CollectionEntry,
        cartItem: CartItemShort,
        discount: Double,
        quantity: Int
    ): CartItem? {
        var newCartItem1 = newCartItem
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
            "firstName" to userData.firstName,
            "lastName" to userData.lastName,
            "email" to userData.email,
            "phoneNumber" to userData.phoneNumber,
            "address" to userData.address,
            "zipCode" to userData.zipCode,
            "city" to userData.city,
            "state" to userData.state,
            "favoriteItems" to userData.favoriteItems.toList(),
            "cartItems" to userData.cartItem.toList()
        )
    }

    fun userData(hashMap: MutableMap<String, Any>?): UserData? {
        if (hashMap == null) {
            return null
        }
        return UserData(
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
            cartItem = extractCartItem(hashMap = hashMap)
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
        val items = hashMap["cartItems"]
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


data class FavoriteItem(
    val productId: String,
    val category: String,
)

data class CartItemShort(
    val productId: String,
    val category: String,
)

data class UserData(
    var firstName: String = "",
    var lastName: String = "",
    var email: String,
    var phoneNumber: String = "",
    var address: String = "",
    var zipCode: String = "",
    var city: String = "",
    var state: String = "",
    var favoriteItems: Set<FavoriteItem> = setOf(),
    var cartItem: List<CartItem> = listOf<CartItem>(),
)

data class TagData(
    val productDescription: String,
    val productImages: List<String>,
    val productName: String,
    val productPrice: Double,
    val id: String,
)

data class Review(
    val date: String,
    val rating: Double,
    val reviewBody: String,
    val title: String,
)

data class CollectionEntry(
    val id: String,
    val link: String,
    val tagData: TagData,
    val reviews: List<Review>,
)