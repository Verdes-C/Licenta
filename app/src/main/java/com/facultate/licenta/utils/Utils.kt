package com.facultate.licenta.utils

import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.Review
import com.facultate.licenta.model.UserData
import java.util.Locale

object Utils {

    fun calculateRating(reviews: List<Review>): Double {
        if (reviews.isEmpty()) {
            return 0.0
        }
        val totalRating = reviews.sumOf { it.rating / 2 }
        return totalRating / reviews.size
    }

    fun calculateCartTotalPrice(cartList: List<CartItem>): Double {
        var total = 0.0
        cartList.forEach { cartItem ->
            total += (cartItem.productPrice - cartItem.productDiscount*cartItem.productPrice)*cartItem.productQuantity
        }
        return String.format(Locale.US,"%.2f",total).toDouble()
    }

    fun calculateTotal(price: Double, quantity: Int): Double {
        val total = price * quantity
        val roundedTotal = String.format(Locale.US, "%.2f", total)
        return roundedTotal.toDouble()
    }

    fun getFullAddress(userData: UserData): String{
        return "${userData.address} ${userData.zipCode} ${userData.city} ${userData.state}"
    }

}