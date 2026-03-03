package com.ia.financias.data.model

import kotlinx.serialization.Serializable
import androidx.compose.ui.graphics.Color
import com.ia.financias.ui.theme.*

@Serializable
enum class TransactionType {
    income, expense
}

@Serializable
enum class TransactionCategory(
    val label: String,
    val emoji: String,
    val color: Color
) {
    alimentacao("Alimentação", "🍔", CategoryFood),
    transporte("Transporte", "🚗", CategoryTransport),
    moradia("Moradia", "🏠", CategoryHome),
    saude("Saúde", "💊", CategoryHealth),
    lazer("Lazer", "🎬", CategoryLisure),
    educacao("Educação", "📚", CategoryEducation),
    vestuario("Vestuário", "👕", CategoryClothing),
    servicos("Serviços", "🔧", CategoryServices),
    investimentos("Investimentos", "📈", CategoryOthers), // Ajustar se necessário
    salario("Salário", "💰", IncomeGreen),
    freelance("Freelance", "💼", TealPrimary),
    outros("Outros", "📦", CategoryOthers)
}

@Serializable
enum class PaymentMethod(val label: String, val emoji: String) {
    pix("Pix", "📲"),
    credito("Crédito", "💳"),
    debito("Débito", "💳"),
    dinheiro("Dinheiro", "💵"),
    boleto("Boleto", "📄"),
    transferencia("Transferência", "🏦")
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
    val payment_method: String? = null,
    val credit_card_id: String? = null,
    val created_at: String? = null
)

@Serializable
data class CreditCard(
    val id: String? = null,
    val user_id: String? = null,
    val name: String,
    val brand: String,
    val color: String,
    val credit_limit: Double,
    val closing_day: Int,
    val due_day: Int
)
