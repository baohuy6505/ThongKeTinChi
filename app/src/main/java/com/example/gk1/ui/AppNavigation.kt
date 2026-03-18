package com.example.gk1.ui
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gk1.ui.HomeScreen
import com.example.gk1.ui.InvoiceScreen
import com.example.gk1.viewmodel.MainViewModel

@Composable
fun AppNavigation(viewModel: MainViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {

        // Màn hình 1
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }

        // Màn hình 2 (Nhận parameters)
        composable(
            route = "invoice/{theory}/{practice}",
            arguments = listOf(
                navArgument("theory") { type = NavType.IntType },
                navArgument("practice") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val theory = backStackEntry.arguments?.getInt("theory") ?: 0
            val practice = backStackEntry.arguments?.getInt("practice") ?: 0
            InvoiceScreen(navController = navController, theoryCredits = theory, practiceCredits = practice)
        }
    }
}