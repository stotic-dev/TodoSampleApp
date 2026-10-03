package com.example.todosampleapp.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion

/** Android モジュール共通の SDK / Java バージョン設定 */
internal fun CommonExtension.configureKotlinAndroid() {
    compileSdk {
        version =
            release(36) {
                minorApiLevel = 1
            }
    }

    defaultConfig.minSdk = 24

    compileOptions.sourceCompatibility = JavaVersion.VERSION_11
    compileOptions.targetCompatibility = JavaVersion.VERSION_11
}
