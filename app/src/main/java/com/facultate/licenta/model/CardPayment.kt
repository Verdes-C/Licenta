package com.facultate.licenta.model


data class CardPayment(
    val nameOnCard: String = "",
    val cardNumber: String = "",
    val expirationDate: String = "",
    val csvString:String = ""
)