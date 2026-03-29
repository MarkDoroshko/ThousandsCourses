plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Dependency Injection
    implementation(libs.javax.inject)

    // JUnit
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)

    // Mockito
    testImplementation(libs.mockito.kotlin)

    // Coroutines Test
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.test {
    useJUnitPlatform()
}
