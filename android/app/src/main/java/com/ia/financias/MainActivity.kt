package com.ia.financias

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ia.financias.ui.auth.AuthScreen
import com.ia.financias.ui.dashboard.DashboardScreen
import com.ia.financias.ui.cards.CardsScreen
import com.ia.financias.ui.transactions.TransactionsScreen
import com.ia.financias.ui.goals.GoalsScreen
import com.ia.financias.ui.family.FamilyScreen
import com.ia.financias.ui.reports.ReportsScreen
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
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    val items = listOf(
        NavigationItem("dashboard", "Dashboard", Icons.Default.Home),
        NavigationItem("transactions", "Transações", Icons.Default.History),
        NavigationItem("cards", "Cartões", Icons.Default.CreditCard),
        NavigationItem("reports", "Relatórios", Icons.Default.BarChart),
        NavigationItem("goals", "Metas", Icons.Default.Flag),
        NavigationItem("family", "Família", Icons.Default.Group)
    )

    Scaffold(
        bottomBar = {
            if (currentDestination != "auth") {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title, fontSize = 10.sp) },
                            selected = currentDestination == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId)
                                    launchSingleTop = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController, 
            startDestination = "auth",
            modifier = Modifier.padding(padding)
        ) {
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
            composable("transactions") {
                TransactionsScreen(onNewTransaction = { navController.navigate("dashboard") })
            }
            composable("cards") {
                CardsScreen(cards = emptyList(), onAddCard = {})
            }
            composable("reports") {
                ReportsScreen(onExportPdf = {})
            }
            composable("goals") {
                GoalsScreen()
            }
            composable("family") {
                FamilyScreen()
            }
        }
    }
}

data class NavigationItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
