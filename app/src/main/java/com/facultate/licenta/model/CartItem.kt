package com.facultate.licenta.model

data class CartItem(
    val productId: String,
    val productName: String,
    val productImage: String,
    val productImageDescription: String,
    val productPrice: Double,
    val productDiscount: Double,
    val productCategory: String,
    var productQuantity: Int = 1,
    val rating: Double,
)