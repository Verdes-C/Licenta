package com.facultate.licenta.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.components.DisplayVoucher
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Variables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VouchersPage(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                menuEntry = MenuEntries.Vouchers, displayArrow = true, navController = navController
            ) {}
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxSize()
                .background(color = Variables.grey1)
                .padding(paddingValues = paddingValues)
                .padding(horizontal = Variables.outerItemGap),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            repeat(5) {
                item { DisplayVoucher() }
            }
        }
    }
}