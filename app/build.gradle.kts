import org.jetbrains.kotlin.ir.backend.js.compile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.djhb.petopia"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.djhb.petopia"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
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
        viewBinding = true
        dataBinding = true
    }


}



dependencies {
    implementation("com.github.Yasic:ParticleTextView:77a4f4c4a8")
    implementation("com.vanniktech:android-image-cropper:4.5.0")
    implementation("com.codemybrainsout.onboarding:onboarder:1.0.4")
    implementation("com.github.mmin18:RealtimeBlurView:82df3520b4")
    implementation("com.github.siralam:DropDownView:b664b916de")
    implementation("com.github.RiccardoMoro:RMSwitch:v1.2.2")
    implementation("com.github.MatteoBattilana:WeatherView:3.0.0")
    implementation("com.github.wenchaojiang:AndroidSwipeableCardStack:0.1.6")
    implementation("com.ramotion.foldingcell:folding-cell:1.2.3")
    implementation("com.wajahatkarim:easyflipviewpager:2.0.1")
    implementation("com.github.skydoves:elasticviews:2.1.0")
    implementation("com.github.bloderxd:deck:0.1")
    implementation("com.github.developer-shivam:FeaturedRecyclerView:1.0.0")
    implementation("com.github.iamporus:TypedTextView:1.1.2")
    implementation("io.github.muddz:styleabletoast:2.4.0")
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("androidx.fragment:fragment-ktx:1.8.1")
    implementation("androidx.activity:activity-ktx:1.6.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("androidx.viewpager2:viewpager2:1.1.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("com.github.skydoves:powerspinner:1.2.6")
    implementation ("me.relex:circleindicator:2.1.6")

    implementation(libs.firebase.database.ktx)
    implementation(libs.firebase.dataconnect)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    implementation(libs.firebase.auth)

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //TextInputLayout
    implementation("com.google.android.material:material:1.9.0")


    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-appcheck-safetynet:16.0.0")

    //to splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")
    //to workmanager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-auth:22.0.0")
    implementation("com.google.firebase:firebase-auth-ktx:22.0.0")
    implementation("com.google.gms:google-services:4.3.15")

    implementation("com.google.android.material:material:1.7.0")
    implementation("com.ssomai:android.scalablelayout:2.1.6")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}