package com.ia.financias.data.model

import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color

@Serializable
enum class TransactionType { income, expense }

@Serializable
enum class TransactionCategory(val label: String, val emoji: String, val colorHex: Long) {
    ALIMENTACAO("Alimentação", "🍔", 0xFFF97316),
    TRANSPORTE("Transporte", "🚗", 0xFF8B5CF6),
    MORADIA("Moradia", "🏠", 0xFF06B6D4),
    SAUDE("Saúde", "💊", 0xFFEC4899),
    LAZER("Lazer", "🎬", 0xFFF59E0B),
    EDUCACAO("Educação", "📚", 0xFF3B82F6),
    VESTUARIO("Vestuário", "👕", 0xFFA855F7),
    SERVICOS("Serviços", "🔧", 0xFF6366F1),
    OUTROS("Outros", "📦", 0xFF64748B),
    SALARIO("Salário", "💰", 0xFF22C55E),
    FREELANCE("Freelance", "💼", 0xFF14B8A6),
    INVESTIMENTOS("Investimentos", "📈", 0xFF10B981);
    
    val color: Color get() = Color(colorHex)
}

@Serializable
enum class PaymentMethod(val label: String, val emoji: String) {
    PIX("Pix", "📲"),
    CREDITO("Crédito", "💳"),
    DEBITO("Débito", "💳"),
    DINHEIRO("Dinheiro", "💵"),
    BOLETO("Boleto", "📄"),
    TRANSFERENCIA("Transferência", "🏦")
}

@Serializable
data class Transaction(
    val id: String? = null,
    val user_id: String? = null,
    val family_group_id: String? = null,
    val type: TransactionType,
    val amount: Double,
    val category: TransactionCategory,
    val description: String,
    val date: String,
    val payment_method: PaymentMethod? = null,
    val credit_card_id: String? = null,
    val installments: Int? = null,
    val created_at: String? = null
)

@Serializable
data class CreditCard(
    val id: String? = null,
    val user_id: String? = null,
    val name: String,
    val brand: String,
    val color: String, // Hex string
    val credit_limit: Double,
    val closing_day: Int,
    val due_day: Int,
    val family_group_id: String? = null
)

@Serializable
data class FinancialGoal(
    val id: String? = null,
    val user_id: String? = null,
    val name: String,
    val target_amount: Double,
    val current_amount: Double,
    val goal_type: String, // "savings" ou "limit"
    val category: TransactionCategory? = null,
    val deadline: String? = null,
    val family_group_id: String? = null
)
