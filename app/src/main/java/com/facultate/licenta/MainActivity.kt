package com.facultate.licenta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.facultate.licenta.navigation.BottomNav
import com.facultate.licenta.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            viewModel.updateUserData()
            //! Remove
//            viewModel.addOrUpdateToHomeSection(
//                products = products,
//                section = "Find Something New"
//            )
            val screens = listOf(
                Screens.HomePage to ImageVector.vectorResource(id = R.drawable.icon_home),
                Screens.Categories to ImageVector.vectorResource(id = R.drawable.icon_category),
                Screens.Cart to ImageVector.vectorResource(id = R.drawable.icon_cart),
                Screens.Favorites to ImageVector.vectorResource(id = R.drawable.icon_favorites),
                Screens.Profile to ImageVector.vectorResource(id = R.drawable.icon_profile)
            )
            val navController = rememberNavController()
            BottomNav(screens = screens, navController = navController)
        }
    }
}
//val products = listOf(
//    SpecialProduct(
//        productId = "5ddd5794-007f-439c-a805-ea1817a288e8",
//        category = "Flexible Nib",
//    ),SpecialProduct(
//        productId = "52e88300-f22f-4322-a48e-7fa975d59d2d",
//        category = "Flexible Nib",
//    ),SpecialProduct(
//        productId = "97731607-484b-4e91-9114-27835415611b",
//        category = "Flexible Nib",
//    ),
//)

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Greeting("Android")
}