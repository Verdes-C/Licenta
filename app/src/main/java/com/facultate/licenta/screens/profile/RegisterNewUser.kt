package com.facultate.licenta.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.Logo
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.validateEmail
import com.facultate.licenta.utils.validatePassword
import kotlinx.coroutines.launch

@Composable
fun RegisterNewUser(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    var usernameOrEmail by remember {
        mutableStateOf("")
    }

    var usernameOrEmailValidationFailed by remember {
        mutableStateOf(false to "")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordValidationFailed by remember {
        mutableStateOf(false to "")
    }

    var passwordTest by remember {
        mutableStateOf("")
    }

    var tosAccepted by remember {
        mutableStateOf(false)
    }

    var tosAcceptedValidationFailed by remember {
        mutableStateOf(false to "")
    }

    var showWarning by remember {
        mutableStateOf(false to "")
    }

    val isAuth by viewModel.isAuth.collectAsState()

    val message by viewModel.exceptionMessage.collectAsState()

    if (isAuth == ApplicationState.AuthState.Authenticated) {
        navController.navigate(Screens.Profile.route)
    } else {
        LazyColumn(
            modifier = Modifier
                .background(color = Variables.grey1)
                .fillMaxSize()
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Logo()
                    Text(text = "Discover the art of handwriting", style = Typography.h3)
                    Text(text = "Register", style = Typography.h3)
                }

                Divider(thickness = 2.dp, color = Variables.grey6)
            }

            item {
                //_ Register form
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height((LocalConfiguration.current.screenHeightDp - 260 - 100).dp)
                        .padding(all = Variables.outerItemGap),
                    verticalArrangement = Arrangement.Center
                ) {
                    CustomTextField(
                        modifier = Modifier.padding(bottom = Variables.innerItemGap),
                        isError = usernameOrEmailValidationFailed.second.isNotEmpty(),
                        label = usernameOrEmailValidationFailed.second.ifEmpty { "Email address" },
                        placeholder = "Input a valid email address",
                        onValueChange = { newValue ->
                            if (usernameOrEmailValidationFailed.first) usernameOrEmailValidationFailed =
                                false to ""
                            usernameOrEmail = newValue
                        }
                    )

                    CustomTextField(
                        modifier = Modifier.padding(bottom = Variables.innerItemGap),
                        isError = passwordValidationFailed.second.isNotEmpty(),
                        label = passwordValidationFailed.second.ifEmpty { "Password" },
                        placeholder = "At least 8 characters, one digit, and one special character",
                        onValueChange = { newValue ->
                            if (passwordValidationFailed.first) passwordValidationFailed =
                                false to ""
                            password = newValue
                        },
                        isPassword = true
                    )

                    CustomTextField(
                        modifier = Modifier.padding(bottom = Variables.innerItemGap),
                        isError = passwordValidationFailed.second.isNotEmpty(),
                        label = "Repeat password",
                        placeholder = "Repeat password",
                        onValueChange = { newValue ->
                            passwordTest = newValue
                        },
                        isPassword = true
                    )

                    if (message.isNotEmpty()) {
                        Text(
                            text = "Error: $message", style = Typography.p.copy(
                                color = Color.Red
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (
                        showWarning.first
                    ) {
                        Text(
                            text = showWarning.second,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = Variables.outerItemGap),
                            textAlign = TextAlign.Center,
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "I accept the TOS", style = Typography.p)
                        Buttons.Checkbox(isChecked = tosAccepted) {
                            tosAccepted = !tosAccepted
                        }
                    }

                    Buttons.PrimaryActive(text = "Register", modifier = Modifier.fillMaxWidth()) {
                        //TODO register and login
                        if (
                            !validateEmail(usernameOrEmail)
                        ) {
                            usernameOrEmailValidationFailed =
                                true to "Please insert a valid email address"
                        } else if (
                            !validatePassword(password)
                        ) {
                            passwordValidationFailed =
                                true to "At least 8 characters, one capital letter, one digit and one special character"
                        } else if (password != passwordTest) {
                            passwordValidationFailed = true to "Passwords to not match"
                        } else if (tosAccepted == false) {
                            showWarning = true to "Please accept the Terms of Service"
                        } else if (validateEmail(usernameOrEmail) && validatePassword(password) && password == passwordTest && tosAccepted) {
                            viewModel.viewModelScope.launch {
                                viewModel.signUpUsingCredentials(
                                    email = usernameOrEmail,
                                    password = password
                                )
                            }
                        }
                    }
                }
            }
        }
    }


}