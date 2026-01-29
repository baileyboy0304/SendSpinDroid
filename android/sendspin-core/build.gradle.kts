plugins {
    id("com.android.library")
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
    namespace = "com.sendspindroid"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
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
    implementation("androidx.core:core-ktx:1.17.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.media3:media3-session:1.9.0")
    implementation("androidx.media3:media3-common:1.9.0")
    implementation("io.coil-kt:coil:2.7.0")
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("io.getstream:stream-webrtc-android:1.3.4")
    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")
}
