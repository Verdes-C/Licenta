package com.facultate.licenta.model

data class Order(
    val useremail: String,
    val orderNumber: Int,
    val totalPrice: Double,
    val fullAdress: String,
    val products: List<CartItem>,
    val status: OrderStatus = OrderStatus.AwaitingPayment,
)

sealed class OrderStatus {
    object AwaitingPayment : OrderStatus()
    object Paid : OrderStatus()
    object AwaitingShipment : OrderStatus()
    object Shipped : OrderStatus()
    object InDelivery : OrderStatus()
    object Delivered : OrderStatus()
}