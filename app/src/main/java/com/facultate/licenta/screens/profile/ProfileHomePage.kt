package com.facultate.licenta.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.Logo
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.MenuEntry
import com.facultate.licenta.components.SocialLoginPlatforms
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.model.UserData
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.redux.ApplicationState
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.facultate.licenta.utils.validateEmail
import com.facultate.licenta.utils.validatePassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ProfileHomePage(
    navController: NavHostController,
    viewModel: ProfileViewModel = hiltViewModel(),
    logout: () -> Unit,
    googleSignIn: () -> Unit,
) {

    val context = LocalContext.current

    val entriesAndNavigation =
        listOf(
            MenuEntries.Orders to {
                navController.navigate(Screens.Orders.route)
            },
            MenuEntries.Vouchers to {
                navController.navigate(Screens.Vouchers.route)
            },
            MenuEntries.Favorites to {
                navController.navigate(Screens.Favorites.route)
            },
            MenuEntries.AccountData to {
                navController.navigate(Screens.AccountData.route)
            },
            MenuEntries.ShippingAdress to {},
            MenuEntries.Support to {},
            MenuEntries.Logout to {
                logout.invoke()
                navController.navigate(Screens.Profile.route)
            },
        )

    val userIsAuth by viewModel.isAuth.collectAsState()
    val userData by viewModel.userData.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.viewModelScope.launch {
            viewModel.readUserData()
        }
    }

    LazyColumn(
        modifier = Modifier
            .background(color = Variables.grey1)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Variables.outerItemGap),
        horizontalAlignment = Alignment.Start,
    ) {
        if (userIsAuth == ApplicationState.AuthState.Authenticated) {
            displayLoggedInUser(
                lazyListScope = this,
                navController = navController,
                userData = userData!!,
                entriesAndNavigation = entriesAndNavigation
            )
        } else {
            item {
                DisplayLoginPage(
                    navController = navController,
                    viewModel = viewModel,
                    googleSignIn = googleSignIn
                )
            }
        }
    }
}

@Composable
fun DisplayLoginPage(
    navController: NavHostController,
    viewModel: ProfileViewModel,
    googleSignIn: () -> Unit,
) {
    val context = LocalContext.current

    val exception by viewModel.exceptionMessage.collectAsState()

    var email by remember {
        mutableStateOf("")
    }

    var emailValidationFailed by remember {
        mutableStateOf(
            false to ""
        )
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordValidationFailed by remember {
        mutableStateOf(
            false to ""
        )
    }

    var keepLoggedIn by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Logo()
        Text(text = "Let your words flourish", style = Typography.h3)
        Text(text = "Log in", style = Typography.h3)
    }

    Divider(thickness = 2.dp, color = Variables.grey6)

    //_ login form
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = Variables.outerItemGap),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
    ) {
        CustomTextField(
            label = emailValidationFailed.second.ifEmpty { "Email" },
            placeholder = "example@gmail.com",
            isError = emailValidationFailed.first,
            modifier = Modifier.fillMaxWidth()
        ) { newValue ->
            if (emailValidationFailed.first) emailValidationFailed = false to ""
            email = newValue
        }

        if (exception.isNotEmpty()) {
            Text(
                text = "Error: $exception", style = Typography.p.copy(
                    color = Color.Red
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        CustomTextField(
            label = passwordValidationFailed.second.ifEmpty { "Password" },
            isError = passwordValidationFailed.first,
            placeholder = "password",
            modifier = Modifier.fillMaxWidth(),
            isPassword = true
        ) { newValue ->
            if (passwordValidationFailed.first) passwordValidationFailed = false to ""
            password = newValue
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
            ) {
                Text(text = "Keep me logged in?", style = Typography.p)
                Buttons.Checkbox(isChecked = keepLoggedIn) {
                    keepLoggedIn = !keepLoggedIn
                }
            }
            Buttons.Tertiary(
                modifier = Modifier.align(alignment = Alignment.CenterEnd),
                text = "Forgot password?",
                textStyle = Typography.buttonBold.copy(
                    color = Variables.blue3
                )
            ) {
                //TODO go to reset password
                navController.navigate(Screens.ResetPassword.route)
            }
        }
        Buttons.PrimaryActive(modifier = Modifier.fillMaxWidth(), text = "Log in") {
            //TODO validation
            if (!validateEmail(email)) {
                emailValidationFailed = true to "Please input a valid email address"
                return@PrimaryActive
            }
            if (!validatePassword(password)) {
                passwordValidationFailed = true to "Password format does not match. Try again"
                return@PrimaryActive
            }
            if (
                validateEmail(email) && validatePassword(password)
            ) {
                viewModel.viewModelScope.launch {
                    viewModel.logInWithEmailAndPassword(
                        email = email,
                        password = password,
                        context = context
                    )
                }
                //todo validation for wrong credentials
            }
        }

        Buttons.SecondaryActive(modifier = Modifier.fillMaxWidth(), text = "Sign up") {
            //TODO go to sign up
            navController.navigate(Screens.RegisterUser.route)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGap)
        ) {
            Buttons.SocialLogin(
                modifier = Modifier.weight(1f),
                socialPlatform = SocialLoginPlatforms.Google
            ) {
                CoroutineScope(Dispatchers.Main).launch {
                    googleSignIn.invoke()
                }
            }
//            Buttons.SocialLogin(
//                modifier = Modifier.weight(1f),
//                socialPlatform = SocialLoginPlatforms.Facebook
//            ) {
//                //TODO Facebook Login
//            }
        }
    }

}

fun displayLoggedInUser(
    lazyListScope: LazyListScope,
    navController: NavHostController,
    userData: UserData,
    entriesAndNavigation: List<Pair<MenuEntry, () -> Any>>,
) {
    lazyListScope.item {
        ProfileUserHeading(
            modifier = Modifier
                .padding(top = Variables.outerItemGap)
                .padding(horizontal = Variables.outerItemGap),
            userData = userData
        ) {
            navController.navigate(Screens.AccountData.route)
        }
    }

    //_ Profile cards
    lazyListScope.items(items = entriesAndNavigation) { menu ->

        TopBar(
            modifier = Modifier.padding(horizontal = Variables.outerItemGap),
            menuEntry = menu.first,
            navigate = {
                menu.second.invoke()
            },
        )
    }
    if (userData.accountType == "user") {
        lazyListScope.item {
            Text(
                modifier = Modifier.padding(horizontal = Variables.outerItemGap),
                text = "InkQuill is giving their regards to our dear customers!",
                textAlign = TextAlign.Center,
                style = Typography.h2,
                color = Variables.blue3
            )
        }
    } else if (userData.accountType == "admin") {
        lazyListScope.item {
            TopBar(
                modifier = Modifier.padding(horizontal = Variables.outerItemGap),
                menuEntry = MenuEntries.AdminOrders,
                isAdmin = userData.accountType == "admin"
            ) {
                navController.navigate(Screens.AdminOrders.route)
            }
        }
    }


}

@Composable
fun ProfileUserHeading(modifier: Modifier = Modifier, userData: UserData, onEditClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor
            )
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(horizontal = Variables.innerItemGap)
    ) {

        Text(
            text = if (userData.firstName.isNotEmpty() || userData.lastName.isNotEmpty()) "${userData.firstName} ${userData.lastName}" else "Proud User",
            style = Typography.h4,
            modifier = Modifier
                .weight(1f)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
        Icon(
            painter = painterResource(id = R.drawable.icon_edit),
            contentDescription = "Edit name",
            tint = Variables.blue3,
            modifier = Modifier.clickable {
                onEditClick.invoke()
            }
        )
    }
}
