package com.facultate.licenta.screens.admins

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.Repository
import com.facultate.licenta.model.Order
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: Repository,
) : ViewModel() {

    private var _adminOrdersToEdit: MutableStateFlow<List<Order>> = MutableStateFlow(listOf())
    val adminOrdersToEdit: StateFlow<List<Order>> = _adminOrdersToEdit

    suspend fun fetchOrders() = viewModelScope.launch {
        _adminOrdersToEdit.value = repository.getUnfulfilledOrders()
    }

    fun updateOrderStatus(updatedOrder: Order) = viewModelScope.launch {
        repository.updateOrder(updatedOrder = updatedOrder)
        _adminOrdersToEdit.value = _adminOrdersToEdit.value.mapNotNull { order ->
            if(order.orderNumber != updatedOrder.orderNumber){
                return@mapNotNull order
            }else{
                null
            }
        }

    }

}