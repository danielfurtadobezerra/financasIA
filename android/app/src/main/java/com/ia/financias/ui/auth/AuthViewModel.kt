package com.ia.financias.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class AuthViewModel(private val supabase: SupabaseClient) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    init {
        // Verificar se já existe uma sessão ativa
        val session = supabase.auth.currentSessionOrNull()
        if (session != null) {
            _isAuthenticated.value = true
        }
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                supabase.auth.signInWith(Email) {
                    this.email = email
                    password = pass
                }
                _isAuthenticated.value = true
            } catch (e: Exception) {
                _error.value = "Erro ao entrar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(name: String, email: String, pass: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                supabase.auth.signUpWith(Email) {
                    this.email = email
                    password = pass
                    data = buildJsonObject {
                        put("full_name", name)
                    }
                }
                _error.value = "Verifique seu e-mail para confirmar o cadastro!"
            } catch (e: Exception) {
                _error.value = "Erro ao cadastrar: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
