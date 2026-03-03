package com.ia.financias.ui.cards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ia.financias.data.model.CreditCard

@Composable
fun CardsScreen(
    cards: List<CreditCard>,
    onAddCard: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddCard,
                containerColor = Color(0xFF14B8A6),
                contentColor = Color.White
            ) {
                Text("+ Novo Cartão")
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
                    "Meus Cartões",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(cards) { card ->
                CreditCardItem(card, spent = 1250.0) // Exemplo fixo de gasto
            }
        }
    }
}

@Composable
fun CreditCardItem(card: CreditCard, spent: Double) {
    val cardColor = Color(android.graphics.Color.parseColor(card.color))
    val usagePercent = (spent / card.credit_limit).toFloat()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        shadowElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(card.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(card.brand, color = Color.White.copy(alpha = 0.8f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text("Limite Utilizado", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    "R$ ${String.format("%.2f", spent)}", 
                    color = Color.White, 
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
                Text(
                    " / R$ ${String.format("%.0f", card.credit_limit)}", 
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = usagePercent,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            
            Text(
                "${(usagePercent * 100).toInt()}% utilizado",
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
                fontSize = 12.sp
            )
        }
    }
}
