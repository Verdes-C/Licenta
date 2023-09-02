package com.facultate.licenta.model


data class UserData(
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
//    var cardPayment: CardPayment = CardPayment() SOON
)