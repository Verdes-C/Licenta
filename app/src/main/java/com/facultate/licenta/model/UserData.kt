package com.facultate.licenta.model


data class UserData(
    val accountType: String = "user",
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
    var orders: List<Order> = listOf(),
    var fcmToken: String = ""
//    var cardPayment: CardPayment = CardPayment() SOON
)