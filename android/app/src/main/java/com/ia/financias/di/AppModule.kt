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

val appModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "https://lkqjehgrcevxildhpyhw.supabase.co",
            supabaseKey = "sb_publishable_-r29lHJy3s4bdQBNd86W7w_EmRj2fXP"
        ) {
            install(Auth)
            install(Postgrest)
            install(Functions)
            install(Realtime)
        }
    }

    viewModel { AuthViewModel(get()) }
    viewModel { DashboardViewModel(get()) }
    viewModel { CardsViewModel(get()) }
    viewModel { GoalsViewModel(get()) }
}
