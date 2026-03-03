package com.ia.financias.ui.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ia.financias.ui.theme.TealPrimary

@Composable
fun FamilyScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            "Minha Família",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        // Estado sem grupo (exemplo)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Você ainda não tem um grupo familiar", fontWeight = FontWeight.SemiBold)
                Text(
                    "Compartilhe despesas e metas com quem você ama.",
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                Button(
                    onClick = { /* Criar Grupo */ },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.GroupAdd, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Criar Grupo")
                }
            }
        }

        Text("Membros", style = MaterialTheme.typography.titleLarge)
        
        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                MemberItem("Você (Titular)", "titular")
            }
        }
    }
}

@Composable
fun MemberItem(name: String, role: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(TealPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = TealPrimary)
            }
            
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(name, fontWeight = FontWeight.Bold)
                Text(role.uppercase(), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
        }
    }
}
