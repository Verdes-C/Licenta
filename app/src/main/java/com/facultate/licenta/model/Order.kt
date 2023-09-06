package com.facultate.licenta.model

import java.util.UUID

data class Order(
    val userEmail: String,
    val orderNumber: UUID,
    val totalPrice: Double,
    val fullAddress: String,
    val products: List<CartItem>,
    var status: OrderStatus = OrderStatus.Paid,
)

sealed class OrderStatus {
    object AwaitingPayment : OrderStatus(){
        override fun toString(): String {
            return "AwaitingPayment"
        }
    }
    object Paid : OrderStatus(){
        override fun toString(): String {
            return "Paid"
        }
    }
    object Shipped : OrderStatus(){
        override fun toString(): String {
            return "Shipped"
        }
    }
    object Delivered : OrderStatus(){
        override fun toString(): String {
            return "Delivered"
        }
    }
}