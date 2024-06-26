package com.facultate.licenta.hilt.interfaces

import android.content.Context
import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.CartItemShort
import com.facultate.licenta.model.DataState
import com.facultate.licenta.model.FavoriteItem
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.Product
import com.facultate.licenta.model.UserData
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Flow

interface FirebaseRepository {
    suspend fun signUpUsingEmailAndPassword(
        viewModelScope: CoroutineScope,
        email: String,
        password: String
    ): String

    suspend fun retrieveUserData(
        email: String,
    ): UserData?

    suspend fun updateUserData(
        userData: UserData
    )

    suspend fun getSpecialProducts(collection: String): List<Product>

    suspend fun getSearchProducts(category: String?, searchInput: String?):DataState<List<Product>>
    suspend fun getCartItem(
        cartItem: CartItemShort,
        discount: Double,
        quantity: Int
    ): CartItem

    suspend fun getProduct(productCategory: String, productId: String): DataState<Product>

    suspend fun getRecommendedProducts(category: String = "Find Something New"): DataState<List<Product>>

    suspend fun updateRemoteFavorites(
        newFavoriteItems: Set<FavoriteItem>
    )

    suspend fun getFavoriteItems(
        favoriteItems: Set<FavoriteItem>
    ): List<Product>

    suspend fun updateRemoteCart(newCartProducts: List<CartItem>)
    fun saveErrorToDB(exception: Exception)
    fun notifyUserOfError(context: Context, message: String?)
    suspend fun resetPassword(email: String): String

    suspend fun saveOrder(newOrder: Order, email: String)
    suspend fun getUnfulfilledOrders(): List<Order>
    suspend fun updateOrder(updatedOrder: Order)
}
