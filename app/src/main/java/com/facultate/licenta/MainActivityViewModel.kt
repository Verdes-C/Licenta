package com.facultate.licenta

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val store: Store<ApplicationState>,
) : ViewModel() {
    private val auth = Firebase.auth
    fun updateUserData(){
        viewModelScope.launch {
            if(auth.currentUser != null){
                store.update {applicationState ->
                    return@update applicationState.copy(
                        authState = ApplicationState.AuthState.Authenticated
                    )
                }
            }
        }
    }

}