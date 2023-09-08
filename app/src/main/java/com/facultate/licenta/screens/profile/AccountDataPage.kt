package com.facultate.licenta.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.DisplayCardPaymentDetails
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.model.UserData
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.validateEmail
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDataPage(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {

    val context = LocalContext.current

    val userData = viewModel.userData.collectAsState()


    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    var zipCode by remember {
        mutableStateOf("")
    }
    var city by remember {
        mutableStateOf("")
    }
    var state by remember {
        mutableStateOf("")
    }

    var emailValidationFailed by remember{
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.viewModelScope.launch {
            firstName = userData.value?.firstName ?: ""
            lastName = userData.value?.lastName ?: ""
            email = userData.value?.email ?: ""
            phoneNumber = userData.value?.phoneNumber ?: ""
            address = userData.value?.address ?: ""
            zipCode = userData.value?.zipCode ?: ""
            city = userData.value?.city ?: ""
            state = userData.value?.state ?: ""
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                displayArrow = true,
                menuEntry = MenuEntries.AccountData,
                navController = navController,
            )
            {}
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Variables.grey1)
                .padding(horizontal = Variables.outerItemGap)
                .padding(paddingValues = paddingValues),
            verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap),
        ) {

            //_ Personal information
            item {
                Text(text = "Personal information", style = Typography.h3)
            }

            item {
                CustomTextField(
                    label = "First name",
                    placeholder = "First name",
                    initialValue = firstName
                ) { newValue ->
                    firstName = newValue
                    println(firstName)
                }
            }

            item {
                CustomTextField(
                    label = "Last name",
                    placeholder = lastName,
                    initialValue = lastName
                ) { newValue ->
                    lastName = newValue
                }
            }

            item {
                CustomTextField(
                    label = "Email",
                    placeholder = email,
                    initialValue = email,
                    isError = emailValidationFailed
                ) { newValue ->
                    email = newValue
                }
            }

            item {
                CustomTextField(
                    label = "Phone number",
                    placeholder = phoneNumber,
                    initialValue = phoneNumber
                ) { newValue ->
                    phoneNumber = newValue
                }
            }

            //_ Shipping information
            item {
                Text(text = "Main Shipping address", style = Typography.h4)
            }
            item {
                CustomTextField(
                    label = "Address",
                    placeholder = address,
                    initialValue = address
                ) { newValue ->
                    address = newValue
                }
            }

            item {
                CustomTextField(
                    label = "ZIP code",
                    placeholder = zipCode,
                    initialValue = zipCode
                ) { newValue ->
                    zipCode = newValue
                }
            }

            item {
                CustomTextField(
                    label = "City",
                    placeholder = city,
                    initialValue = city
                ) { newValue ->
                    city = newValue
                }
            }

            item {
                CustomTextField(
                    label = "State",
                    placeholder = state,
                    initialValue = state
                ) { newValue ->
                    state = newValue
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Buttons.PrimaryActive(modifier = Modifier.fillMaxWidth(), text = "Save data") {
                        //TODO  save data
                        if(!validateEmail(email  = email)){
                            Toast.makeText(
                                context, "Please provide a valid email address", Toast.LENGTH_LONG
                            ).show()
                            emailValidationFailed = true
                            return@PrimaryActive
                        }
                        viewModel.viewModelScope.launch {
                            viewModel.updateUserDetails(
                                userData = UserData(
                                    accountType = userData.value?.accountType?: "user",
                                    firstName = firstName,
                                    lastName = lastName,
                                    email = email,
                                    phoneNumber = phoneNumber,
                                    address = address,
                                    zipCode = zipCode,
                                    city = city,
                                    state = state,
                                    favoriteItems = userData.value!!.favoriteItems,
                                    cartItem = userData.value!!.cartItem,
                                )
                            )
                            Toast.makeText(
                                context, "Data saved successfully", Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .height(Variables.outerItemGap)
                        .background(color = Color.Transparent)
                )
            }
        }
    }
}


fun formatCreditCardNumber(input: String): String {
    val digitsOnly = input.filter { it.isDigit() }

    return digitsOnly.chunked(4)
        .joinToString(" ")
}
