import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.com.google.dagger.hilt.android)
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
    id("androidx.navigation.safeargs.kotlin")
}

android {
    val properties = Properties();
    properties.load(project.rootProject.file("local.properties").inputStream())

    namespace = "com.joohnq.sppublicbus"
    compileSdk = 34

    testOptions.unitTests.isReturnDefaultValues = true
    testOptions.unitTests.isIncludeAndroidResources = true

    defaultConfig {
        applicationId = "com.joohnq.sppublicbus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.joohnq.sppublicbus.CustomTestRunner"

        buildConfigField(
            "String",
            "API_GOOGLE_MAPS_KEY",
            properties.getProperty("API_GOOGLE_MAPS_KEY")
        )
        buildConfigField("String", "API_SP_TRANS_KEY", properties.getProperty("API_SP_TRANS_KEY"))
        buildConfigField(
            "String",
            "API_DIRECTIONS_KEY",
            properties.getProperty("API_DIRECTIONS_KEY")
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            resValue("string", "clear_text_config", "false")
        }
        getByName("debug") {
            isMinifyEnabled = false
            resValue("string", "clear_text_config", "true")
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
        buildConfig = true
        dataBinding = true
    }

    packaging {
        resources.excludes.add("META-INF/LICENSE.md")
        resources.excludes.add("META-INF/LICENSE-notice.md")
    }
}

dependencies {
    implementation(libs.android.spinkit)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.converter.gson)
    implementation(libs.core)
    implementation(libs.expandablebottombar)
    implementation(libs.hilt.android)
    implementation(libs.hilt.android.testing)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.logging.interceptor)
    implementation(libs.material)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.android)
    implementation(libs.play.services.maps)
    implementation(libs.retrofit)

    kapt(libs.hilt.android.compiler)
    kaptAndroidTest(libs.hilt.android.compiler)

    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.junit.ktx)
    testImplementation(libs.androidx.rules)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)

    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.junit.ktx)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.mockwebserver)
    androidTestImplementation(libs.truth)

    testAnnotationProcessor(libs.hilt.android.compiler)
    androidTestAnnotationProcessor(libs.hilt.android.compiler)
    testImplementation(libs.robolectric)
    androidTestImplementation(libs.robolectric)
    implementation(libs.powermock)
    testImplementation(libs.powermock)

    implementation(libs.byte.buddy)
    testImplementation(libs.byte.buddy)
    androidTestImplementation(libs.byte.buddy)

    implementation(libs.objenesis)
    testImplementation(libs.objenesis)
    androidTestImplementation(libs.objenesis)

    implementation(libs.mockito.inline)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.mockito.inline)

    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation(libs.androidx.espresso.intents)
    androidTestImplementation(libs.androidx.espresso.idling.resource)

    testImplementation(libs.androidx.espresso.core)
    testImplementation(libs.androidx.espresso.contrib)
    testImplementation(libs.androidx.espresso.intents)
    testImplementation(libs.androidx.espresso.idling.resource)
}