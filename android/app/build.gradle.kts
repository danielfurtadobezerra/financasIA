plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.ia.financias"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ia.financias"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2026.02.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.navigation:navigation-compose:2.8.5")
    implementation("androidx.compose.material:material-icons-extended")

    // Supabase
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.4.1")
    implementation("io.github.jan-tennert.supabase:auth-kt:3.4.1")
    implementation("io.github.jan-tennert.supabase:functions-kt:3.4.1")
    implementation("io.github.jan-tennert.supabase:realtime-kt:3.4.1")

    // Koin
    implementation("io.insert-koin:koin-androidx-compose:3.5.0")

    // Coil (Images)
    implementation("io.coil-kt:coil-compose:2.5.0")

    // Ktor (Required for Supabase)
    implementation("io.ktor:ktor-client-android:3.0.1")
    implementation("io.ktor:ktor-client-core:3.0.1")
    implementation("io.ktor:ktor-client-logging:3.0.1")
    implementation("io.ktor:ktor-client-content-negotiation:3.0.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.1")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.10.0")

    // PDF Generation
    implementation("com.itextpdf:itext7-core:7.1.15")
}

configurations.all {
    resolutionStrategy {
        force("androidx.browser:browser:1.8.0")
    }
}
