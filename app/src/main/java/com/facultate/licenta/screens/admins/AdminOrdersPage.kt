package com.facultate.licenta.screens.admins

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.DisplayAdminOrder
import com.facultate.licenta.components.DisplayOrderItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOrdersPage(
    navController: NavHostController,
    viewModel: AdminViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    val orders by viewModel.adminOrdersToEdit.collectAsState()


    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.AdminOrders,
                isAdmin = true,
                navController = navController,
            )
            {}
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Variables.grey1)
                .padding(paddingValues = paddingValues)
                .padding(
                    horizontal = Variables.outerItemGap,
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            if (orders.isEmpty()) {
                item { Text(text = "There are no new orders", style = Typography.h4, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) }
            } else {
                items(items = orders) { order ->
                    val status = order.status
                    DisplayAdminOrder(
                        orderItem = order,
                        status = status,
                        navController = navController,
                        indexOutOf = "${orders.indexOf(order) + 1}/${orders.size}"
                    ) { updatedOrder ->
                        viewModel.updateOrderStatus(updatedOrder = updatedOrder)
                        Toast.makeText(context, "Order status modified", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}