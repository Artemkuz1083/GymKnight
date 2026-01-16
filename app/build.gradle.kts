plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")


    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.gymknight"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.gymknight"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    val voyager  = "1.0.1"
    implementation("cafe.adriel.voyager:voyager-navigator:$voyager")
    implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyager")
    implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyager")
    implementation("cafe.adriel.voyager:voyager-transitions:$voyager")

    val koin = "4.1.1"
    implementation("io.insert-koin:koin-android:$koin")
    implementation("io.insert-koin:koin-compose:$koin")
    implementation("io.insert-koin:koin-androidx-compose:$koin")

    implementation("cafe.adriel.voyager:voyager-koin:$voyager")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")


    //room
    implementation("androidx.room:room-runtime:2.8.4")
    implementation("androidx.room:room-ktx:2.8.4")
    ksp("androidx.room:room-compiler:2.8.4")

    //ИКОНКИ
    implementation("androidx.compose.material:material-icons-extended")

    //JSON
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}