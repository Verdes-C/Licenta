package com.facultate.licenta.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.components.DisplayOrderItem
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.OrderItem
import com.facultate.licenta.components.OrderStatus
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Variables

@Composable
fun Orders(navController: NavHostController) {


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Variables.grey1)
            .padding(horizontal = Variables.outerItemGap),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        item {
            TopBar(modifier = Modifier.padding(top = Variables.outerItemGap),menuEntry = MenuEntries.Orders, displayArrow = true, navController = navController) {
            }
        }

        item {
            DisplayOrderItem(orderItem = OrderItem())
        }

        item {
            DisplayOrderItem(orderItem = OrderItem(status = OrderStatus.Paid))
        }

        item {
            DisplayOrderItem(orderItem = OrderItem(status = OrderStatus.Shipped))
        }

        item {
            DisplayOrderItem(orderItem = OrderItem(status = OrderStatus.Delivered))
        }
    }
}