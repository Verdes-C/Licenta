package com.facultate.licenta.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val store: Store<ApplicationState>,
) : ViewModel() {
    private val auth = Firebase.auth
    var isAuth = MutableStateFlow(isAuth())


    fun signUpUsingCredentials(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    logInWithEmailAndPassword(email = email, password = password)
                }
            }

        }
    }

    fun logInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch {
            store.update { applicationState ->
                return@update applicationState.copy(
                    authState = ApplicationState.AuthState.Authenticated,
                    userData = UserData(
                        email = email,
                    )
                )
            }
            auth.signInWithEmailAndPassword(email, password)
            isAuth.value = true
        }
    }

    fun logout(){
        viewModelScope.launch {
            store.update { applicationState ->
                return@update applicationState.copy(
                    authState = ApplicationState.AuthState.Unauthenticated(),
                    userData = UserData(
                    )
                )
            }
            auth.signOut()
            isAuth.value = false
        }
    }

    private fun isAuth(): Boolean {
        var isAuth = false
        viewModelScope.launch {
            store.read {
                isAuth = it.authState == ApplicationState.AuthState.Authenticated
            }
        }
        return isAuth
    }


}