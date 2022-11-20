plugins {
  id("java-library")
  id("org.jetbrains.kotlin.jvm")
  kotlin("kapt")
}

java {
  sourceCompatibility = JavaVersion.VERSION_1_10
  targetCompatibility = JavaVersion.VERSION_1_10
}

dependencies {
  implementation("com.google.code.gson:gson:2.8.7")
  implementation("com.squareup.retrofit2:retrofit:2.9.0")
  implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

  // Moshi
  implementation("com.squareup.moshi:moshi:1.14.0")
  kapt("com.squareup.moshi:moshi-kotlin-codegen:1.14.0")
}

kapt {
  correctErrorTypes = true
}