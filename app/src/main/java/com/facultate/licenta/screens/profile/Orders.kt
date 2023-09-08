package com.facultate.licenta.screens.profile

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.DisplayOrderItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Variables

@Composable
fun Orders(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val orders by viewModel.orders.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Variables.grey1)
            .padding(horizontal = Variables.outerItemGap)
            .padding(bottom = Variables.outerItemGap),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            TopBar(
                modifier = Modifier.padding(top = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.Orders,
                navController = navController,
            )
            {}
        }

        items(items = orders) { order ->
            DisplayOrderItem(orderItem = order, navController = navController)
        }

    }
}