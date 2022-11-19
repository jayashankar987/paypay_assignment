plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
  kotlin("kapt")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.paypay.framework"
  compileSdk = 33

  defaultConfig {
    minSdk = 23
    targetSdk = 33

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles = "consumer-rules.pro"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }
  compileOptions {
    sourceCompatibility =  JavaVersion.VERSION_1_1
    targetCompatibility = JavaVersion.VERSION_1_1
  }
  kotlinOptions {
    jvmTarget = "11"
  }
}

dependencies {
  implementation(project(":data"))
  implementation("androidx.core:core-ktx:1.9.0")
  implementation("androidx.appcompat:appcompat:1.5.1")
  implementation("com.google.android.material:material:1.7.0")
  testImplementation("junit:junit:4.13.2")
  androidTestImplementation("androidx.test.ext:junit:1.1.4")
  androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")

  implementation("com.google.dagger:hilt-android:2.44")
  kapt("com.google.dagger:hilt-android-compiler:2.44")

  implementation("com.squareup.retrofit2:retrofit:2.9.0")

  implementation("androidx.room:room-runtime:2.4.3")
  annotationProcessor("androidx.room:room-compiler:2.4.3")

  implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
  implementation("com.squareup.moshi:moshi:1.14.0")
}