package com.facultate.licenta.screens.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.GoogleAuthUiClient
import com.facultate.licenta.firebase.SignInResult
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.GoogleSignInStatus
import com.facultate.licenta.model.UserData
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: FirebaseRepository,
) : ViewModel() {
    private val auth = Firebase.auth

    var isAuth: StateFlow<ApplicationState.AuthState> =
        store.stateFlow.map { it.authState }.distinctUntilChanged().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            ApplicationState.AuthState.Unauthenticated()
        )
    var userData = store.stateFlow.map { it.userData }.distinctUntilChanged().stateIn(
        viewModelScope,
        SharingStarted.Eagerly, null
    )
    var exceptionMessage: MutableStateFlow<String> = MutableStateFlow("")

    suspend fun signUpUsingCredentials(email: String, password: String) {
        val response = repository.signUpUsingEmailAndPassword(
            viewModelScope = viewModelScope,
            email = email,
            password = password
        )
        if (response == "") {
            Log.d("REGISTER", response)
            actions.updateUserData(userData = UserData(email = email))
        } else if (response == "Not verified") {
            exceptionMessage.value = "Please verify your email before logging in."
        }
    }

    suspend fun logInWithEmailAndPassword(email: String, password: String, context: Context) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            viewModelScope.launch {
                val userData = repository.retrieveUserData(
                    email = email,
                )
                actions.updateUserData(userData = userData)
                store.read { applicationState ->
                    viewModelScope.launch {
                        repository.updateRemoteCart(newCartProducts = applicationState.cartProducts)
                        repository.updateRemoteFavorites(newFavoriteItems = applicationState.favoriteItems.toSet())
                    }
                }
            }
        }.addOnFailureListener { e ->
            repository.notifyUserOfError(context = context, message = e.message)
            repository.saveErrorToDB(exception = e)
            exceptionMessage.value = e.message!!
        }
    }

    fun onSignInResult(result: SignInResult) {
        if (result.data != null) {
            println("GOOGLE SIGNIN  ${result.data}")
        }
        exceptionMessage.value = result.errorMessage ?: ""
    }

    suspend fun readUserData(): UserData? {
        val toReturn = store.read {
            it.userData
        }
        return toReturn

    }

    suspend fun updateUserDetails(userData: UserData) = viewModelScope.launch {
        var newUserData: UserData?
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
                cartItem = oldUserData.cartItem
            )
            return@update applicationState.copy(
                userData = newUserData
            )
        }
        repository.updateUserData(userData = userData)
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        exceptionMessage.value = repository.resetPassword(email = email)
    }

}