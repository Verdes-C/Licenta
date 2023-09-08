package com.facultate.licenta.screens.categories

import androidx.lifecycle.ViewModel
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.redux.Store
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
) : ViewModel() {

    fun calculateRoute(category: String): String {
        return "search/${
            category.split(" ").map {
                it.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
            }.joinToString(" ")
        }/null"
    }

}