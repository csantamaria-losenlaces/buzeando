plugins {
    id("com.android.library") version "8.3.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    kotlin("kapt")
}

android {
    namespace = "com.carlossantamaria.buzeando"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.carlossantamaria.buzeando"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        buildConfig = true
    }

    secrets {
        // Optionally specify a different file name containing your secrets.
        // The plugin defaults to "local.properties"
        propertiesFileName = "secrets.properties"

        // A properties file containing default secret values. This file can be
        // checked in version control.
        defaultPropertiesFileName = "local.defaults.properties"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.volley)

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.23"))

    // Google Maps Places
    implementation("com.google.android.libraries.places:places:3.4.0")

    // Dependencia de Google Play Services para Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")

    // okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    implementation("androidx.activity:activity-compose:1.9.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

kapt {
    correctErrorTypes = true
}