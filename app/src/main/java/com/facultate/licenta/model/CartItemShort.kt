package com.facultate.licenta.model

data class CartItemShort(
    val productId: String,
    val category: String,
    val quantity: Int = 1
)