import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

val tmdbBearerToken = localProperties.getProperty("TMDB_BEARER_TOKEN")?.takeIf { it.isNotBlank() && it != "null" }
    ?: "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MDY4ZjhkYjgwYWE3NzExOWFmNTdhNzRlMjEyYzE3ZSIsIm5iZiI6MTc4OTAxMjE1MC40ODg5OTk4LCJzdWIiOiI2YWEyMjhiNmUyY2QxN2QxOGQ2NjgyNDIiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.IQhHqtrlfhkxyEnnCpGaR8Z721rx8a6TRj0Jh6eZ_uQ"
val tmdbApiKey = localProperties.getProperty("TMDB_API_KEY")?.takeIf { it.isNotBlank() && it != "null" }
    ?: "4068f8db80aa77119af57a74e212c17e"

android {
    namespace = "com.cinedex.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cinedex.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "TMDB_BEARER_TOKEN", "\"$tmdbBearerToken\"")
        buildConfigField("String", "TMDB_API_KEY", "\"$tmdbApiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.paging)
    ksp(libs.androidx.room.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi.kotlin)

    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    implementation(libs.coil.compose)
    implementation(libs.timber)
    implementation(libs.amdx.material.icons.extended)

    debugImplementation(libs.androidx.ui.tooling)
}
