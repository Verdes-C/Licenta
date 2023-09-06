package com.facultate.licenta.screens.Search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.model.Product
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: FirebaseRepository,
) : ViewModel() {

    val searchResults: StateFlow<List<Product>> =
        store.stateFlow.map { it.searchResults }.distinctUntilChanged().stateIn(
            viewModelScope, SharingStarted.Eagerly, listOf()
        )

    fun getResults(category: String?, searchInput: String) = viewModelScope.launch {
        actions.updateSearchResults(
            searchResultsList = repository.getSearchProducts(
                category = category,
                searchInput = searchInput
            )
        )
    }
}