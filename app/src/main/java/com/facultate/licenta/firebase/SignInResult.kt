package com.facultate.licenta.firebase

data class SignInResult(
    val data: GoogleUserData?,
    val errorMessage: String?
)

data class GoogleUserData(
    val userId: String,
    val email: String?,
    val profilePictureUrl: String?
)