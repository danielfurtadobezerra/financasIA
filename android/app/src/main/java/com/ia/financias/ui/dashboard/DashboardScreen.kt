package com.ia.financias.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ia.financias.data.model.Transaction
import com.ia.financias.ui.theme.*

@Composable
fun DashboardScreen(
    userName: String,
    balance: Double,
    income: Double,
    expenses: Double,
    transactions: List<Transaction>,
    onSendIA: (String) -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    var previewTransaction by remember { mutableStateOf<com.ia.financias.data.model.Transaction?>(null) }

    Scaffold(
        topBar = {
            DashboardHeader(userName)
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                SummarySection(balance, income, expenses)
            }

            item {
                AIInputSection(
                    text = inputText,
                    onValueChange = { inputText = it },
                    onSend = { 
                        // Simulação de processamento de IA
                        previewTransaction = com.ia.financias.data.model.Transaction(
                            description = inputText,
                            amount = 50.0,
                            category = com.ia.financias.data.model.TransactionCategory.ALIMENTACAO,
                            type = com.ia.financias.data.model.TransactionType.expense,
                            date = "hoje"
                        )
                        inputText = ""
                    }
                )
            }

            if (previewTransaction != null) {
                item {
                    PreviewTransactionCard(
                        transaction = previewTransaction!!,
                        onConfirm = {
                            // Aqui salvaria no Supabase
                            previewTransaction = null
                        },
                        onCancel = { previewTransaction = null }
                    )
                }
            }

            item {
                InsightsCard(
                    message = "Você gastou 15% a mais em Alimentação do que no mês passado. Que tal cozinhar em casa hoje?"
                )
            }

            item {
                Text(
                    text = "Transações Recentes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(transactions) { transaction ->
                TransactionItem(transaction)
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun SummarySection(balance: Double, income: Double, expenses: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Card Principal Saldo
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .background(Brush.linearGradient(PrimaryGradient))
                    .padding(24.dp)
            ) {
                Text("Saldo Total", color = Color.White.copy(alpha = 0.8f))
                Text(
                    "R$ ${String.format("%.2f", balance)}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummarySmallCard("Receitas", income, IncomeGreen, Modifier.weight(1f))
            SummarySmallCard("Despesas", expenses, ExpenseRed, Modifier.weight(1f))
        }
    }
}

@Composable
fun SummarySmallCard(label: String, value: Double, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(
                "R$ ${String.format("%.2f", value)}",
                color = color,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIInputSection(text: String, onValueChange: (String) -> Unit, onSend: () -> Unit) {
    Column {
        TextField(
            value = text,
            onValueChange = onValueChange,
            placeholder = { Text("Paguei 50 reais de almoço hoje...") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TealPrimary)
            },
            trailingIcon = {
                Row(modifier = Modifier.padding(end = 8.dp)) {
                    IconButton(onClick = { /* Voz */ }) {
                        Icon(Icons.Default.Mic, contentDescription = null, tint = ExpenseRed)
                    }
                    IconButton(onClick = onSend) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = TealPrimary)
                    }
                }
            }
        )
        Text(
            "💡 Dica: Clique no microfone ou digite algo como 'Gastei 89 no iFood ontem'",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp, start = 4.dp)
        )
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(transaction.category.color.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(transaction.category.emoji, fontSize = 24.sp)
            }
            
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(transaction.description, fontWeight = FontWeight.SemiBold)
                Text(transaction.date, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }

            val isExpense = transaction.type == com.ia.financias.data.model.TransactionType.expense
            Text(
                "${if (isExpense) "-" else "+"} R$ ${String.format("%.2f", transaction.amount)}",
                color = if (isExpense) ExpenseRed else IncomeGreen,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PreviewTransactionCard(
    transaction: com.ia.financias.data.model.Transaction,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        border = androidx.compose.foundation.BorderStroke(1.dp, TealPrimary.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = TealPrimary)
                Text(
                    "Confirmar Lançamento", 
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    color = TealPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = transaction.description,
                onValueChange = {}, // Editável no futuro
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(modifier = Modifier.padding(top = 12.dp).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = "R$ ${transaction.amount}",
                    onValueChange = {},
                    label = { Text("Valor") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = transaction.category.label,
                    onValueChange = {},
                    label = { Text("Categoria") },
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancelar", color = ExpenseRed)
                }
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirmar")
                }
            }
        }
    }
}

@Composable
fun DashboardHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Olá,", color = Color.Gray)
            Text(userName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
        // Ícone de Perfil/Logout
    }
}

@Composable
fun InsightsCard(message: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = WarningYellow.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarningYellow.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = WarningYellow)
            Text(
                text = message,
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.bodySmall,
                color = TextPrimaryLight
            )
        }
    }
}
