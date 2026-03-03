package com.ia.financias.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ia.financias.data.model.Transaction
import com.ia.financias.data.model.TransactionType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import io.github.jan.supabase.functions.functions
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class DashboardViewModel(private val supabase: SupabaseClient) : ViewModel() {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions

    private val _previewTransaction = MutableStateFlow<Transaction?>(null)
    val previewTransaction: StateFlow<Transaction?> = _previewTransaction

    fun processIA(text: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = supabase.functions.invoke("parse-transaction", buildJsonObject {
                    put("query", text)
                })
                val parsed = response.decodeAs<Transaction>()
                _previewTransaction.value = parsed
            } catch (e: Exception) {
                e.printStackTrace()
                // Fallback local simples se a IA falhar
                _previewTransaction.value = Transaction(
                    description = text,
                    amount = 0.0,
                    category = com.ia.financias.data.model.TransactionCategory.OUTROS,
                    type = TransactionType.expense,
                    date = "2026-03-03"
                )
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearPreview() {
        _previewTransaction.value = null
    }

    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

    private val _income = MutableStateFlow(0.0)
    val income: StateFlow<Double> = _income

    private val _expenses = MutableStateFlow(0.0)
    val expenses: StateFlow<Double> = _expenses

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val userEmail: String? get() = supabase.auth.currentSessionOrNull()?.user?.email

    fun fetchData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = supabase.postgrest["transactions"]
                    .select()
                    .decodeList<Transaction>()
                
                _transactions.value = response.sortedByDescending { it.date }
                calculateTotals(response)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun calculateTotals(list: List<Transaction>) {
        val inc = list.filter { it.type == TransactionType.income }.sumOf { it.amount }
        val exp = list.filter { it.type == TransactionType.expense }.sumOf { it.amount }
        _income.value = inc
        _expenses.value = exp
        _balance.value = inc - exp
    }

    fun saveTransaction(transaction: Transaction) {
        viewModelScope.launch {
            try {
                supabase.postgrest["transactions"].insert(transaction)
                fetchData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    fun logout() {
        viewModelScope.launch {
            supabase.auth.signOut()
        }
    }
}
