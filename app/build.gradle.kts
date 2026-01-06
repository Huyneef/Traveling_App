plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.traveling_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.traveling_app"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    packagingOptions{
        exclude("META-INF/NOTICE.md")
        exclude("META-INF/LICENSE.md")
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.flexbox:flexbox:3.0.0")
    implementation("com.sun.mail:android-mail:1.6.6")
    implementation("com.sun.mail:android-activation:1.6.7")
    //noinspection GradleCompatible
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-database:20.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("io.reactivex.rxjava3:rxjava:3.0.0")
// RxAndroid
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("com.github.momo-wallet:mobile-sdk:1.0.6")
    implementation("com.firebaseui:firebase-ui-database:8.0.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-database")
    implementation ("com.facebook.android:facebook-android-sdk:5.15.3")
    implementation ("com.facebook.android:facebook-android-sdk:[4,5)")
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.facebook.android:facebook-login:12.1.0")


    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("com.google.code.gson:gson:2.10.1")
    
    // Gemini AI SDK
    implementation("com.google.ai.client.generativeai:generativeai:0.1.2")
    
    // Retrofit for API calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
}