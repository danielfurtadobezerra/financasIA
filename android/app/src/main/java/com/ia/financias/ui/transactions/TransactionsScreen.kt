package com.ia.financias.ui.transactions

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ia.financias.data.model.Transaction
import com.ia.financias.ui.dashboard.TransactionItem
import com.ia.financias.ui.theme.TealPrimary

@Composable
fun TransactionsScreen(
    transactions: List<Transaction>,
    onNewTransaction: () -> Unit
) {
    var selectedMonth by remember { mutableStateOf("Março") }
    
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNewTransaction, containerColor = TealPrimary) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Transações",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { /* Filtros */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = null)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Simulação de Lista
            LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Text(
                        "Resumo do Mês ($selectedMonth)",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
                
                if (transactions.isEmpty()) {
                    item {
                        Box(Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nenhum lançamento este mês.", color = Color.Gray)
                        }
                    }
                } else {
                    items(transactions) { transaction ->
                        TransactionItem(transaction)
                    }
                }
            }
        }
    }
}
