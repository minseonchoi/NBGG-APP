plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.choiminseon.nbggapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.choiminseon.nbggapp"
        minSdk = 21
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // 레트로핏 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // gps로 현재위치 가져오는 라이브러리
    implementation ("pub.devrel:easypermissions:3.0.0")
    implementation ("com.google.android.gms:play-services-location:21.0.1")

    // 글라이드 라이브러리
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    // 애니메이션 lottie 라이브러리
    implementation ("com.airbnb.android:lottie:5.2.0")

    // circleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Google Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:18.2.0")
}