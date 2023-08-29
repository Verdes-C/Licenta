package com.facultate.licenta.screens.profile

import android.media.tv.TvContract.Channels.Logo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.Logo
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun RegisterNewUser(navController: NavHostController) {
    var usernameOrEmail by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordTest by remember {
        mutableStateOf("")
    }

    var tosAccepted by remember {
        mutableStateOf(false)
    }


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
            //_ Reset form
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((LocalConfiguration.current.screenHeightDp - 260 - 100).dp)
                    .padding(all = Variables.outerItemGap),
                verticalArrangement = Arrangement.Center
            ) {
                CustomTextField(
                    modifier = Modifier.padding(bottom = Variables.innerItemGap),
                    label = "Username or email",
                    placeholder = "Input your account's username or email",
                    onValueChange = { newValue ->
                        usernameOrEmail = newValue
                    }
                )

                CustomTextField(
                    modifier = Modifier.padding(bottom = Variables.innerItemGap),
                    label = "Password",
                    placeholder = "Password",
                    onValueChange = { newValue ->
                        usernameOrEmail = newValue
                    }
                )

                CustomTextField(
                    modifier = Modifier.padding(bottom = Variables.innerItemGap),
                    label = "Repeat password",
                    placeholder = "Repeat password",
                    onValueChange = { newValue ->
                        usernameOrEmail = newValue
                    }
                )

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
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        navController.navigate(Screens.Profile.route)
                    }
                }
            }
        }
    }
}