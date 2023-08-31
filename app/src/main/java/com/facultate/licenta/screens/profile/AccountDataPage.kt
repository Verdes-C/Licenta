package com.facultate.licenta.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.DisplayCardPaymentDetails
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.UserData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDataPage(navController: NavHostController) {
    val userData = UserData(email = "") //TODO change

    Scaffold(
        topBar = {
            TopBar(
                modifier = Modifier.padding(all = Variables.outerItemGap),
                menuEntry = MenuEntries.AccountData,
                displayArrow = true,
                navController = navController
            ) {}
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

            //_ Profile image
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.image_placeholder),
                            contentDescription = "Profile image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(160.dp)
                                .clip(shape = CircleShape)
                                .align(alignment = Alignment.Center)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.icon_edit),
                            contentDescription = "Edit profile Image",
                            modifier = Modifier.align(alignment = Alignment.TopEnd),
                            tint = Variables.blue3
                        )
                    }
                }
            }

            //_ Personal information
            item {
                Text(text = "Personal information", style = Typography.h3)
            }
            item {
                LazyVerticalGrid(
                    modifier = Modifier.height(124.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    item {
                        CustomTextField(
                            label = "First name",
                            placeholder = "Ion"
                        ) { newValue ->
                            userData.firstName = newValue
                        }
                    }

                    item {
                        CustomTextField(
                            label = "Last name",
                            placeholder = "Ionescu"
                        ) { newValue ->
                            userData.lastName = newValue
                        }
                    }

                    item {
                        CustomTextField(
                            label = "Email",
                            placeholder = "ionionescu@gmail.com"
                        ) { newValue ->
                            userData.email = newValue
                        }
                    }

                    item {
                        CustomTextField(
                            label = "Phone number",
                            placeholder = "0723456789"
                        ) { newValue ->
                            userData.phoneNumber = newValue
                        }
                    }

                }
            }

            //_ Shipping information
            item {
                Text(text = "Main Shipping address", style = Typography.h4)
            }
            item {
                LazyVerticalGrid(
                    modifier = Modifier.height(124.dp),
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        CustomTextField(
                            label = "Address",
                            placeholder = "Str. Lacului, Nr. 2, Bl. X2, Ap.24"
                        ) { newValue ->
                            userData.address = newValue
                        }
                    }

                    item {
                        CustomTextField(
                            label = "ZIP code",
                            placeholder = "032123"
                        ) { newValue ->
                            userData.zipCode = newValue
                        }
                    }

                    item {
                        CustomTextField(label = "City", placeholder = "Bucuresti") { newValue ->
                            userData.city = newValue
                        }
                    }

                    item {
                        CustomTextField(label = "State", placeholder = "Romania") { newValue ->
                            userData.state = newValue
                        }
                    }
                }
            }

            //_ Payment information
            item {
                Text(text = "Main payment method", style = Typography.h4)
            }
            item {
                DisplayCardPaymentDetails(userData = userData)
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Buttons.PrimaryActive(modifier = Modifier.fillMaxWidth(), text = "Save data") {
                        //TODO  save data
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
