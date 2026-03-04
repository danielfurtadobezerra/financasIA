package com.ia.financias.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ia.financias.data.model.FinancialGoal
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GoalsViewModel(private val supabase: SupabaseClient) : ViewModel() {

    private val _goals = MutableStateFlow<List<FinancialGoal>>(emptyList())
    val goals: StateFlow<List<FinancialGoal>> = _goals

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun fetchGoals() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = supabase.postgrest["financial_goals"]
                    .select()
                    .decodeList<FinancialGoal>()
                _goals.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addGoal(goal: FinancialGoal) {
        viewModelScope.launch {
            try {
                val userId = supabase.auth.currentSessionOrNull()?.user?.id
                val goalWithUser = goal.copy(user_id = userId)
                supabase.postgrest["financial_goals"].insert(goalWithUser)
                fetchGoals()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
