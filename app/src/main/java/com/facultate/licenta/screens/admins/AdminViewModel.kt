package com.facultate.licenta.screens.admins

import androidx.lifecycle.ViewModel
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.Order
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: Repository
) : ViewModel() {

//    val orders:StateFlow<List<Order>> = store.stateFlow.map {  }

}