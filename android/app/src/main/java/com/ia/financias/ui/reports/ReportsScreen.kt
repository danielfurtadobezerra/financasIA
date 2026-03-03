package com.ia.financias.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ia.financias.data.model.TransactionCategory
import com.ia.financias.ui.theme.IncomeGreen
import com.ia.financias.ui.theme.TealPrimary

@Composable
fun ReportsScreen(
    onExportPdf: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onExportPdf, containerColor = TealPrimary) {
                Icon(Icons.Default.FileDownload, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Text(
                    "Relatório Mensal",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ReportSummarySection()
            }

            item {
                Text("Gastos por Categoria", style = MaterialTheme.typography.titleLarge)
            }

            // Exemplo de lista de categorias
            items(5) {
                CategoryProgressItem(
                    category = TransactionCategory.alimentacao,
                    spent = 450.0,
                    total = 1000.0
                )
            }
        }
    }
}

@Composable
fun ReportSummarySection() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text("🎉 Parabéns!", color = IncomeGreen, fontWeight = FontWeight.Bold)
            Text(
                "Seu saldo está positivo em R$ 1.200,00 este mês.",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            HorizontalDivider()
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Pior Categoria", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("Alimentação", fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Total Transações", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    Text("24", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun CategoryProgressItem(category: TransactionCategory, spent: Double, total: Double) {
    val progress = (spent / total).toFloat()
    
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(category.emoji, fontSize = 20.sp)
                Text(category.label, modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Medium)
            }
            Text("R$ ${String.format("%.2f", spent)}", fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxWidth().height(8.dp),
            color = category.color,
            trackColor = category.color.copy(alpha = 0.2f),
            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}
