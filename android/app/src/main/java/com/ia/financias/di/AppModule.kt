package com.ia.financias.di

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.realtime.Realtime
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel
import com.ia.financias.ui.auth.AuthViewModel
import com.ia.financias.ui.dashboard.DashboardViewModel
import com.ia.financias.ui.cards.CardsViewModel
import com.ia.financias.ui.goals.GoalsViewModel
import com.ia.financias.ui.dashboard.VoiceToTextParser
import org.koin.android.ext.koin.androidApplication

val appModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://lkqjehgrcevxildhpyhw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImxrcWplaGdyY2V2eGlsZGhweWh3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzI1NTY5MTQsImV4cCI6MjA4ODEzMjkxNH0.Awulrj7-hUoV7zOlxd8b3vBuIo0jTOXjAbIHW4jc-kA"
        ) {
            install(Auth)
            install(Postgrest)
            install(Functions)
            install(Realtime)
        }
    }
    
    single { VoiceToTextParser(androidApplication()) }
 
    viewModel { AuthViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { CardsViewModel(get()) }
    viewModel { GoalsViewModel(get()) }
}
