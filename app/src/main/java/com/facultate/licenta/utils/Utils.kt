package com.facultate.licenta.utils

import com.facultate.licenta.model.CartItem
import com.facultate.licenta.model.Review
import java.util.Locale

object Utils {

    fun calculateRating(reviews: List<Review>): Double {
        if (reviews.isEmpty()) {
            return 0.0 // Handle the case of an empty list to avoid division by zero
        }
        val totalRating = reviews.sumByDouble { it.rating.toDouble() / 2 }
        return totalRating / reviews.size
    }

    fun calculateProductTotalPrice(cartList: List<CartItem>): Double {
        var total = 0.0
        cartList.forEach { cartItem ->
            total += String.format(
                Locale.US,
                "%.2f",
                cartItem.productQuantity * cartItem.productPrice
            ).toDouble()
        }
        return total
    }

    fun calculateTotal(price: Double, quantity: Int): Double {
        val total = price * quantity
        val roundedTotal = String.format(Locale.US, "%.2f", total)
        return roundedTotal.toDouble()
    }


}