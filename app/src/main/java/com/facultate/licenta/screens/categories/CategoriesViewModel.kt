package com.facultate.licenta.screens.categories

import androidx.lifecycle.ViewModel
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
     val store: Store<ApplicationState>,
) : ViewModel() {

}