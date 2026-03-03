package com.ia.financias.ui.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ia.financias.ui.theme.TealPrimary

@Composable
fun GoalsScreen(goals: List<com.ia.financias.data.model.FinancialGoal>) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* Nova Meta */ }, containerColor = TealPrimary) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Text(
                    "Minhas Metas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            items(goals) { goal ->
                GoalCard(
                    name = goal.name,
                    current = goal.current_amount,
                    target = goal.target_amount,
                    color = goal.category?.color ?: Color(0xFF22C55E)
                )
            }
        }
    }
}

@Composable
fun GoalCard(name: String, current: Double, target: Double, color: Color) {
    val progress = (current / target).toFloat()
    
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Flag, contentDescription = null, tint = color)
                Text(name, modifier = Modifier.padding(start = 8.dp), fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("R$ ${String.format("%.2f", current)}", fontSize = 14.sp)
                Text("Alvo: R$ ${String.format("%.2f", target)}", fontSize = 14.sp, color = Color.Gray)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Text(
                "${(progress * 100).toInt()}% concluído",
                modifier = Modifier.padding(top = 4.dp).align(Alignment.End),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}
