package com.facultate.licenta.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.Logo
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.validateEmail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ResetPassword(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    var usernameOrEmail by remember {
        mutableStateOf("")
    }

    val messsage by viewModel.exceptionMessage.collectAsState()

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
                Text(text = "Let your words flourish", style = Typography.h3)
                Text(text = "Reset your password", style = Typography.h3)
            }

            Divider(thickness = 2.dp, color = Variables.grey6)
        }

        item {
            //_ Reset form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp - 260 - 100).dp)
                    .padding(all = Variables.outerItemGap),
                verticalArrangement = Arrangement.Center
            ) {

                if (messsage.isNotEmpty()) {
                    Text(
                        text = "$messsage", style = Typography.p,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                CustomTextField(
                    modifier = Modifier.padding(bottom = Variables.innerItemGap),
                    label = "Username or email",
                    placeholder = "Input your account's username or email",initialValue = "",
                    onValueChange = { newValue ->
                        usernameOrEmail = newValue
                    }
                )
                Buttons.PrimaryActive(modifier = Modifier.fillMaxWidth(), text = "Reset password") {
                    //TODO check and then continue
                    if (validateEmail(email = usernameOrEmail)) {
                        viewModel.resetPassword(email = usernameOrEmail)
                    }
                }
            }
        }
    }
}