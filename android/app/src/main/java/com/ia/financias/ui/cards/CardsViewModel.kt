package com.ia.financias.ui.cards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ia.financias.data.model.CreditCard
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CardsViewModel(private val supabase: SupabaseClient) : ViewModel() {

    private val _cards = MutableStateFlow<List<CreditCard>>(emptyList())
    val cards: StateFlow<List<CreditCard>> = _cards

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchCards() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = supabase.postgrest["credit_cards"]
                    .select()
                    .decodeList<CreditCard>()
                _cards.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addCard(card: CreditCard) {
        viewModelScope.launch {
            try {
                supabase.postgrest["credit_cards"].insert(card)
                fetchCards()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
