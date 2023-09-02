package com.facultate.licenta.model

sealed class GoogleSignInStatus{
    data object Login: GoogleSignInStatus()
    data object Signout: GoogleSignInStatus()
    data object Waiting: GoogleSignInStatus()

}

