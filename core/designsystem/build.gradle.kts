plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.roborazzi)
}

android {
    namespace = "com.sportygroup.weatherapp.core.designsystem"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    // Shares the Roborazzi screenshot-test harness (RoborazziScreenshotTest) with feature modules.
    @Suppress("UnstableApiUsage")
    testFixtures {
        enable = true
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(project(":core:model"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Shared screenshot-test harness (Roborazzi + Robolectric, JVM). Exposed as `api` so consuming
    // modules get the runtime via `testImplementation(testFixtures(project(":core:designsystem")))`.
    testFixturesImplementation(platform(libs.androidx.compose.bom))
    testFixturesImplementation(libs.androidx.compose.ui)
    testFixturesImplementation(libs.androidx.compose.material3)
    testFixturesApi(libs.junit)
    testFixturesApi(libs.robolectric)
    testFixturesApi(libs.androidx.compose.ui.test.junit4)
    testFixturesApi(libs.roborazzi)
    testFixturesApi(libs.roborazzi.compose)
    testFixturesApi(libs.roborazzi.junit.rule)

    // Screenshot tests for this module reuse the shared harness above.
    testImplementation(testFixtures(project(":core:designsystem")))
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
}
