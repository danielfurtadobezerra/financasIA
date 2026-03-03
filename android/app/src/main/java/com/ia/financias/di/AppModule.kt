package com.ia.financias.di

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.functions.Functions
import io.github.jan.supabase.realtime.Realtime
import org.koin.dsl.module

val appModule = module {
    single {
        createSupabaseClient(
            supabaseUrl = "SUA_URL_DO_SUPABASE",
            supabaseKey = "SUA_ANON_KEY"
        ) {
            install(Auth)
            install(Postgrest)
            install(Functions)
            install(Realtime)
        }
    }
}
