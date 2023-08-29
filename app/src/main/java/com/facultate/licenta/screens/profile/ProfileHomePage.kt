package com.facultate.licenta.screens.profile

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.facultate.licenta.R
import com.facultate.licenta.components.Buttons
import com.facultate.licenta.components.CustomTextField
import com.facultate.licenta.components.MenuEntries
import com.facultate.licenta.components.MenuEntry
import com.facultate.licenta.components.SocialLoginPlatforms
import com.facultate.licenta.components.TopBar
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables

@Composable
fun ProfileHomePage(navController: NavHostController) {
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
            MenuEntries.Logout to {},
        )

    var userIsLoggedIn by remember {
        mutableStateOf(false)
    }
    LazyColumn(
        modifier = Modifier
            .background(color = Variables.grey1)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Variables.outerItemGap),
        horizontalAlignment = Alignment.Start,
    ) {
        if (userIsLoggedIn) {
            displayLoggedInUser(
                lazyListScope = this,
                navController = navController,
                entriesAndNavigation = entriesAndNavigation
            )
        } else {
            item {
                DisplayLoginPage(navController = navController) {
                    userIsLoggedIn = true
                }
            }
        }
    }
}

@Composable
fun DisplayLoginPage(
    navController: NavHostController,
    loginTest: () -> Unit,
) {
    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
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
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.icon_logo),
            contentDescription = "Ink Qwill logo",
            modifier = Modifier,
            tint = Variables.blue3
        )
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
            label = "Username",
            placeholder = "username",
            modifier = Modifier.fillMaxWidth()
        ) { newValue -> username = newValue }
        CustomTextField(
            label = "Password",
            placeholder = "password",
            modifier = Modifier.fillMaxWidth()
        ) { newValue -> password = newValue }
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
            //TODO login

            //! update
            loginTest.invoke()
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
                //TODO Google Login
            }
            Buttons.SocialLogin(
                modifier = Modifier.weight(1f),
                socialPlatform = SocialLoginPlatforms.Facebook
            ) {
                //TODO Facebook Login
            }
        }
    }

}

fun displayLoggedInUser(
    lazyListScope: LazyListScope,
    navController: NavHostController,
    entriesAndNavigation: List<Pair<MenuEntry, () -> Unit>>,
) {
    lazyListScope.item {
        ProfileUserHeading(
            modifier = Modifier
                .padding(top = Variables.outerItemGap)
                .padding(horizontal = Variables.outerItemGap)
        ) {
            navController.navigate(Screens.AccountData.route)
        }
    }

    //_ Profile cards
    lazyListScope.items(items = entriesAndNavigation) { menu ->

        TopBar(
            modifier = Modifier.padding(horizontal = Variables.outerItemGap),
            menuEntry = menu.first
        ) {
            menu.second.invoke()
        }
    }
    lazyListScope.item {
        Text(
            modifier = Modifier.padding(horizontal = Variables.outerItemGap),
            text = "InkQuill is giving their regards to our dear customers!",
            textAlign = TextAlign.Center,
            style = Typography.h2,
            color = Variables.blue3
        )
    }

}

@Composable
fun ProfileUserHeading(modifier: Modifier = Modifier, onEditClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Variables.innerItemGapLow, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = Variables.elevation,
                spotColor = Variables.shadowColor,
                ambientColor = Variables.shadowColor
            )
            .clip(shape = RoundedCornerShape(Variables.cornerRadius))
            .background(color = Color.White)
            .padding(horizontal = Variables.innerItemGap)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_placeholder),
            contentDescription = "User Profile Image",
            modifier = Modifier
                .requiredSize(size = 60.dp)
                .clip(shape = CircleShape)
        )

        Text(
            text = "Marvin McKinney",
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
