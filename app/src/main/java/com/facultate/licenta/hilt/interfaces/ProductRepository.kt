package com.facultate.licenta.hilt.interfaces

import com.facultate.licenta.screens.cart.CartItem
import com.facultate.licenta.screens.product.Product
import com.facultate.licenta.utils.CartItemShort
import com.facultate.licenta.utils.FavoriteItem
import com.facultate.licenta.utils.UserData
import kotlinx.coroutines.CoroutineScope

interface ProductRepository {
    suspend fun signUpUsingEmailAndPassword(viewModelScope: CoroutineScope,email:String, password: String)
    suspend fun  retrieveUserData(
        viewModelScope: CoroutineScope,
        email: String,
        password: String
    ): UserData?

    suspend fun updateUserData(
        userData: UserData
    )
    suspend fun getSpecialProducts(collection: String): List<Product>
    suspend fun getCartItem(
        cartItem: CartItemShort,
        discount: Double,
        quantity: Int
    ): CartItem

    suspend fun updateRemoteFavorites(
        newFavoriteItems: Set<FavoriteItem>
    )

    suspend fun getFavoriteItems(viewModelScope: CoroutineScope, favoriteItems: Set<FavoriteItem>): List<Product>

    suspend fun updateRemoteCart(newCartProducts: List<CartItem>)
}
