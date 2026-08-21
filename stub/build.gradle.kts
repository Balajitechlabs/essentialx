plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "stub"
    compileSdk = 37
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        aidl = true
    }
}
