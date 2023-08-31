package com.facultate.licenta.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.facultate.licenta.MainActivityViewModel
import com.facultate.licenta.hilt.interfaces.ProductRepository
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.MappersTo
import com.facultate.licenta.utils.MappersTo.userData
import com.facultate.licenta.utils.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: ProductRepository,
) : ViewModel() {
    private val auth = Firebase.auth

    //    var isAuth: StateFlow<ApplicationState.AuthState> =
//        store.stateFlow.map { it.authState }.stateIn(
//            viewModelScope, SharingStarted.Lazily, ApplicationState.AuthState.Unauthenticated()
//        )
    var isAuth = MutableStateFlow(false)

    suspend fun signUpUsingCredentials(email: String, password: String) {
        repository.signUpUsingEmailAndPassword(
            viewModelScope = viewModelScope,
            email = email,
            password = password
        )
        isAuth.value = true
    }

    suspend fun logInWithEmailAndPassword(email: String, password: String) {
        val userData = repository.logInWithEmailAndPassword(
            viewModelScope = viewModelScope,
            email = email,
            password = password
        )
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            viewModelScope.launch {
                actions.updateUserData(userData = userData)
            }
            isAuth.value = true
        }
    }


    fun logout() {
        viewModelScope.launch {
            store.update { applicationState ->
                return@update applicationState.copy(
                    authState = ApplicationState.AuthState.Unauthenticated(),
                    userData = null,
                    cartProducts = listOf(),
                    favoriteItems = mutableSetOf(),
                )
            }
            auth.signOut()
            isAuth.value = false
        }
    }

    fun isAuth() {
        viewModelScope.launch {
            store.read {
                Log.d("TESTING", it.toString())
                isAuth.value =
                    it.authState != ApplicationState.AuthState.Unauthenticated()
            }
        }
    }


}