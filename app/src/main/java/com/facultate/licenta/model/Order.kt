package com.facultate.licenta.model

import java.util.UUID

data class Order(
    val userEmail: String,
    val orderNumber: UUID,
    val totalPrice: Double,
    val fullAddress: String,
    val products: List<CartItem>,
    val status: OrderStatus = OrderStatus.AwaitingPayment,
)

sealed class OrderStatus {
    object AwaitingPayment : OrderStatus()
    object Paid : OrderStatus()
    object Shipped : OrderStatus()
    object Delivered : OrderStatus()
}