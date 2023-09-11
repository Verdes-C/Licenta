package com.facultate.licenta

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.facultate.licenta.firebase.GoogleAuthUiClient
import com.facultate.licenta.hilt.interfaces.FirebaseRepository
import com.facultate.licenta.navigation.BottomNav
import com.facultate.licenta.navigation.Screens
import com.facultate.licenta.redux.Actions
import com.facultate.licenta.screens.profile.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
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

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val profileViewModel: ProfileViewModel = hiltViewModel()


            val screens = listOf(
                Screens.HomePage to ImageVector.vectorResource(id = R.drawable.icon_home),
                Screens.Categories to ImageVector.vectorResource(id = R.drawable.icon_category),
                Screens.Cart to ImageVector.vectorResource(id = R.drawable.icon_cart),
                Screens.Favorites to ImageVector.vectorResource(id = R.drawable.icon_favorites),
                Screens.Profile to ImageVector.vectorResource(id = R.drawable.icon_profile)
            )
            val navController = rememberNavController()
            BottomNav(
                screens = screens,
                navController = navController,
                lifecycleScope = lifecycleScope,
                googleAuthUiClient = googleAuthUiClient,
                profileViewModel = profileViewModel
            )
        }
    }
}




