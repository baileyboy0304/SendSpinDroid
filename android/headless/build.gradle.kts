plugins {
    id("com.android.application")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "com.sendspindroid.headless"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sendspindroid.headless"
        minSdk = 26
        targetSdk = 36
        versionCode = 43
        versionName = "1.0.43"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
}

dependencies {
    implementation(project(":sendspin-core"))
    implementation("androidx.core:core-ktx:1.17.0")
}
