package com.facultate.licenta.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.hilt.interfaces.ProductRepository
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.utils.UserData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
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
    var userData = store.stateFlow.map { it.userData }.distinctUntilChanged().stateIn(
        viewModelScope,
        SharingStarted.Eagerly, null
    )

    suspend fun signUpUsingCredentials(email: String, password: String) {
        repository.signUpUsingEmailAndPassword(
            viewModelScope = viewModelScope,
            email = email,
            password = password
        )
        isAuth.value = true
    }

    suspend fun logInWithEmailAndPassword(email: String, password: String) {
        val userData = repository.retrieveUserData(
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
                isAuth.value =
                    it.authState != ApplicationState.AuthState.Unauthenticated()
            }
        }
    }

    suspend fun readUserData(): UserData? {
        var toReturn = store.read {
            it.userData
        }
        return toReturn

    }

    suspend fun updateUserDetails(userData: UserData) {
        var newUserData: UserData?
        viewModelScope.launch {
            store.update { applicationState ->
                val oldUserData = applicationState.userData
                newUserData = UserData(
                    firstName = if (userData.firstName.isNotEmpty()) userData.firstName else oldUserData!!.firstName,
                    lastName = if (userData.lastName.isNotEmpty()) userData.lastName else oldUserData!!.lastName,
                    email = if (userData.email.isNotEmpty()) userData.email else oldUserData!!.email,
                    phoneNumber = if (userData.phoneNumber.isNotEmpty()) userData.phoneNumber else oldUserData!!.phoneNumber,
                    address = if (userData.address.isNotEmpty()) userData.address else oldUserData!!.address,
                    zipCode = if (userData.zipCode.isNotEmpty()) userData.zipCode else oldUserData!!.zipCode,
                    city = if (userData.city.isNotEmpty()) userData.city else oldUserData!!.city,
                    state = if (userData.state.isNotEmpty()) userData.state else oldUserData!!.state,
                    favoriteItems = oldUserData!!.favoriteItems,
                    cartItem = oldUserData!!.cartItem
                )
                println(newUserData)
                return@update applicationState.copy(
                    userData = newUserData
                )
            }
            val userData = readUserData()
            repository.updateUserData(userData = userData!!)

        }
    }


}