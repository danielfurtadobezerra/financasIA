package com.ia.financias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ia.financias.ui.auth.AuthScreen
import com.ia.financias.ui.dashboard.DashboardScreen
import com.ia.financias.ui.theme.FinanciasIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanciasIATheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                onLogin = { _, _ -> navController.navigate("dashboard") },
                onRegister = { _, _, _ -> navController.navigate("dashboard") }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                userName = "Usuário",
                balance = 1250.0,
                income = 3000.0,
                expenses = 1750.0,
                transactions = emptyList(),
                onSendIA = {}
            )
        }
    }
}
