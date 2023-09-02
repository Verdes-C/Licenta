package com.facultate.licenta

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.facultate.licenta.firebase.GoogleAuthUiClient
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.navigation.BottomNav
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.screens.profile.ProfileViewModel
import com.facultate.licenta.ui.theme.Typography
import com.facultate.licenta.ui.theme.Variables
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var actions: Actions
    @Inject
    lateinit var repository: FirebaseRepository

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            auth = auth,
            action = actions,
            repository = repository,
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext),
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val profileViewModel : ProfileViewModel = hiltViewModel()
            viewModel.updateUserData()

            val screens = listOf(
                Screens.HomePage to ImageVector.vectorResource(id = R.drawable.icon_home),
                Screens.Categories to ImageVector.vectorResource(id = R.drawable.icon_category),
                Screens.Cart to ImageVector.vectorResource(id = R.drawable.icon_cart),
                Screens.Favorites to ImageVector.vectorResource(id = R.drawable.icon_favorites),
                Screens.Profile to ImageVector.vectorResource(id = R.drawable.icon_profile)
            )
            val navController = rememberNavController()
            BottomNav(screens = screens, navController = navController, lifecycleScope = lifecycleScope, googleAuthUiClient = googleAuthUiClient, profileViewModel = profileViewModel)
        }
    }
}
