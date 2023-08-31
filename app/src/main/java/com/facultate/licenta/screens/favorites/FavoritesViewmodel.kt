package com.facultate.licenta.screens.favorites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facultate.licenta.firebase.FirebaseProductRepository
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.facultate.licenta.screens.product.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewmodel @Inject constructor(
    val store: Store<ApplicationState>,
    private val actions: Actions,
    private val repository: FirebaseProductRepository
) : ViewModel() {
    val auth = Firebase.auth

    var favoriteItems = MutableStateFlow<List<Product>>(listOf())

    suspend fun getFavoriteItems() {
        val favoriteItemsResults = actions.getFavoriteItems(viewModelScope)
        favoriteItems.value = favoriteItemsResults
    }

    suspend fun removeFromFavorite(productId: String, productCategory: String) {
        val newFavoriteItems = actions.removeItemFromFavorites(
            productId = productId,
            productCategory = productCategory
        )
        favoriteItems.value =
            repository.getFavoriteItems(viewModelScope, favoriteItems = newFavoriteItems)
        repository.updateRemoteFavorites(newFavoriteItems = newFavoriteItems)
    }


}