plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "com.udeldev.storyapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.udeldev.storyapp"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    val lifecycleVersion = "2.6.2"
    val retrofitVersion = "2.9.0"
    val loggingInterceptorVersion = "4.9.0"
    val dataStorePreferencesVersion = "1.0.0"
    val kotlinxVersion = "1.6.4"
    val glideVersion = "4.11.0"
    val materialVersion = "1.9.0"
    val servicesMapsVersion = "18.1.0"
    val servicesLocationVersion = "21.0.1"

    implementation ("com.google.android.material:material:$materialVersion")

    implementation("com.google.android.gms:play-services-maps:$servicesMapsVersion")
    implementation("com.google.android.gms:play-services-location:$servicesLocationVersion")

    implementation("com.github.bumptech.glide:glide:$glideVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxVersion")

    implementation("androidx.datastore:datastore-preferences:$dataStorePreferencesVersion")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

    implementation("com.squareup.okhttp3:logging-interceptor:$loggingInterceptorVersion")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}