package com.facultate.licenta.screens.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.GoogleAuthUiClient
import com.facultate.licenta.firebase.SignInResult
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.GoogleSignInStatus
import com.facultate.licenta.model.Order
import com.facultate.licenta.model.UserData
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: FirebaseRepository,
) : ViewModel() {
    private val auth = Firebase.auth

    var isAuth =
        store.stateFlow.map { it.authState }.distinctUntilChanged().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            ApplicationState.AuthState.Unauthenticated()
        )
    var userData = store.stateFlow.map { it.userData }.distinctUntilChanged().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(), null
    )
    var exceptionMessage: MutableStateFlow<String> = MutableStateFlow("")

    val cartCount =
        store.stateFlow.map { it.cartProducts.size }.distinctUntilChanged().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(), 0
        )

    val orders = store.stateFlow.map { it.orders }.distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), listOf())

     fun signUpUsingCredentials(email: String, password: String) = viewModelScope.launch {
        val response = repository.signUpUsingEmailAndPassword(
            viewModelScope = viewModelScope,
            email = email,
            password = password
        )
        if (response == "") {
            Log.d("REGISTER", response)
            actions.updateUserData(userData = UserData(email = email))
        } else{
            exceptionMessage.value = response
        }
    }

     fun logInWithEmailAndPassword(email: String, password: String, context: Context) = viewModelScope.launch{
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            viewModelScope.launch {
                val userData = repository.retrieveUserData(
                    email = email,
                )
                val token = FirebaseMessaging.getInstance().token.await()
                if (userData != null) {
                    if(userData.fcmToken != token) {
                        userData.fcmToken = token
                        repository.updateUserData(userData = userData)
                    }
                }
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
        if (result.data?.email != null) {
            viewModelScope.launch {
                val userData = repository.retrieveUserData(
                    email = result.data.email,
                )
                val token = FirebaseMessaging.getInstance().token.await()
                if (userData != null) {
                    if(userData.fcmToken != token) {
                        userData.fcmToken = token
                        repository.updateUserData(userData = userData)
                    }
                }
                actions.updateUserData(userData = userData)
                store.read { applicationState ->
                    viewModelScope.launch {
                        repository.updateRemoteCart(newCartProducts = applicationState.cartProducts)
                        repository.updateRemoteFavorites(newFavoriteItems = applicationState.favoriteItems.toSet())
                    }
                }
            }
        }
        exceptionMessage.value = result.errorMessage ?: ""
    }


     fun updateUserDetails(userData: UserData) = viewModelScope.launch {
        var newUserData: UserData? = null
        store.update { applicationState ->
            val oldUserData = applicationState.userData
            newUserData = UserData(
                accountType = userData.accountType.ifBlank { oldUserData!!.accountType },
                firstName = userData.firstName.ifBlank { oldUserData!!.firstName },
                lastName = userData.lastName.ifBlank { oldUserData!!.lastName },
                email = userData.email.ifBlank { oldUserData!!.email },
                phoneNumber = userData.phoneNumber.ifBlank { oldUserData!!.phoneNumber },
                address = userData.address.ifBlank { oldUserData!!.address },
                zipCode = userData.zipCode.ifBlank { oldUserData!!.zipCode },
                city = userData.city.ifBlank { oldUserData!!.city },
                state = userData.state.ifBlank { oldUserData!!.state },
                favoriteItems = oldUserData!!.favoriteItems,
                cartItem = oldUserData.cartItem,
                fcmToken = oldUserData.fcmToken
            )
            return@update applicationState.copy(
                userData = newUserData
            )
        }
        newUserData?.let { repository.updateUserData(userData = it) }
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        exceptionMessage.value = repository.resetPassword(email = email)
    }

}