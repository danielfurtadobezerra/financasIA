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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ia.financias.ui.auth.AuthScreen
import com.ia.financias.ui.auth.AuthViewModel
import com.ia.financias.ui.dashboard.DashboardScreen
import com.ia.financias.ui.dashboard.DashboardViewModel
import com.ia.financias.ui.cards.CardsScreen
import com.ia.financias.ui.cards.CardsViewModel
import com.ia.financias.ui.transactions.TransactionsScreen
import com.ia.financias.ui.goals.GoalsScreen
import com.ia.financias.ui.goals.GoalsViewModel
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

    val authViewModel: AuthViewModel = koinViewModel()
    val dashboardViewModel: DashboardViewModel = koinViewModel()

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    
    // Auto-navegação se já estiver logado
    LaunchedEffect(isAuthenticated) {
        if (isAuthenticated && currentDestination == "auth") {
            navController.navigate("dashboard") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

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
            if (currentDestination != "auth" && currentDestination != null) {
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
            startDestination = if (isAuthenticated) "dashboard" else "auth",
            modifier = Modifier.padding(padding)
        ) {
            composable("auth") {
                AuthScreen(
                    viewModel = authViewModel
                )
            }
            composable("dashboard") {
                val transactions by dashboardViewModel.transactions.collectAsState()
                val balance by dashboardViewModel.balance.collectAsState()
                val income by dashboardViewModel.income.collectAsState()
                val expenses by dashboardViewModel.expenses.collectAsState()
                val previewTransaction by dashboardViewModel.previewTransaction.collectAsState()
                
                LaunchedEffect(Unit) {
                    dashboardViewModel.fetchData()
                }

                DashboardScreen(
                    userName = dashboardViewModel.userEmail?.split("@")?.get(0) ?: "Usuário",
                    balance = balance,
                    income = income,
                    expenses = expenses,
                    transactions = transactions,
                    previewTransaction = previewTransaction,
                    onSendIA = { dashboardViewModel.processIA(it) },
                    onConfirmTransaction = { 
                        dashboardViewModel.saveTransaction(it)
                        dashboardViewModel.clearPreview()
                    },
                    onCancelPreview = { dashboardViewModel.clearPreview() },
                    onLogout = {
                        dashboardViewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0)
                        }
                    }
                )
            }
            composable("transactions") {
                val transactions by dashboardViewModel.transactions.collectAsState()
                TransactionsScreen(
                    transactions = transactions,
                    onNewTransaction = { navController.navigate("dashboard") }
                )
            }
            composable("cards") {
                val viewModel: CardsViewModel = koinViewModel()
                val cards by viewModel.cards.collectAsState()
                
                LaunchedEffect(Unit) {
                    viewModel.fetchCards()
                }

                CardsScreen(
                    cards = cards,
                    onAddCard = { viewModel.addCard(it) }
                )
            }
            composable("reports") {
                ReportsScreen(onExportPdf = {})
            }
            composable("goals") {
                val viewModel: GoalsViewModel = koinViewModel()
                val goals by viewModel.goals.collectAsState()
                
                LaunchedEffect(Unit) {
                    viewModel.fetchGoals()
                }

                GoalsScreen(goals = goals)
            }
            composable("family") {
                FamilyScreen()
            }
        }
    }
}

data class NavigationItem(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
