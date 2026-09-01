plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.poo4_2p_guerrero_marquez_palaguachi"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.poo4_2p_guerrero_marquez_palaguachi"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/NOTICE.md"
            excludes += "/META-INF/LICENSE.md"
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("com.sun.mail:android-mail:1.6.7")
    implementation("com.sun.mail:android-activation:1.6.7")
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
}
tasks.register<Javadoc>("generateJavadoc") {
    val javaCompile = tasks.named<JavaCompile>("compileDebugJavaWithJavac")
    source = javaCompile.get().source
    classpath = javaCompile.get().classpath

    (options as StandardJavadocDocletOptions).apply {
        encoding = "UTF-8"
        charSet = "UTF-8"
        addStringOption("Xdoclint:none", "-quiet")

        // 1. Cambia el título principal ("app API")
        docTitle = "Documentación del Proyecto POO"
        windowTitle = "Documentación POO"

        // 2. Crea grupos personalizados para la tabla principal
        group("Activities", listOf("com.example.poo4_2p_guerrero_marquez_palaguachi*"))
        group("Modelo de Datos", listOf("Modelo*"))
    }

    exclude("**/R.java", "**/BuildConfig.java")
}