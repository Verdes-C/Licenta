package com.facultate.licenta.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val store: Store<ApplicationState>,
) : ViewModel() {
    private val auth = Firebase.auth
    val fireStore = Firebase.firestore
    var isAuth = MutableStateFlow(isAuth())


    fun signUpUsingCredentials(email: String, password: String) {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { result ->
                if (result.isSuccessful) {
                    logInWithEmailAndPassword(email = email, password = password)
                }
            }.await()

            fireStore.collection("Users")
                .document(email)
                .set(
                    MappersTo.mapOfUserData(
                        userData = UserData(
                            email = email
                        )
                    ),
                    SetOptions.merge()  //_ Create or merge data
                )
                .addOnSuccessListener {
                    Log.d("TESTING", "saved")
                }
                .await()

        }
    }

    fun logInWithEmailAndPassword(email: String, password: String) {
        var userData: UserData? = null
        fireStore.collection("Users")
            .document(email)
            .get()
            .addOnSuccessListener { result ->
                val document = result.data
                if (document != null) {
                    userData = userData(document)
                }
            }
        viewModelScope.launch {
            store.update { applicationState ->
                return@update applicationState.copy(
                    authState = ApplicationState.AuthState.Authenticated,
                    userData = userData
                )
            }
            auth.signInWithEmailAndPassword(email, password)
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

    private fun isAuth(): Boolean {
        var isAuth = false
        viewModelScope.launch {
            store.read {
                isAuth = if (it.authState == ApplicationState.AuthState.Unauthenticated()) false else true
            }
        }
        return isAuth
    }
}