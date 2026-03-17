/*
 * Copyright (c) 2026 Meshtastic LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import com.android.build.api.dsl.ApplicationExtension

plugins {
    alias(libs.plugins.meshtastic.android.application)
    alias(libs.plugins.meshtastic.android.application.flavors)
    alias(libs.plugins.meshtastic.android.application.compose)
    id("meshtastic.koin")
}

configure<ApplicationExtension> {
    namespace = "org.meshtastic.wear"

    defaultConfig {
        applicationId = "com.geeksville.mesh.wear"
        // Target WearOS 6.0 (Android 16)
        minSdk = 30 
        targetSdk = 36
    }

    productFlavors {
        configureEach {
            if (name == "google") {
                applicationId = "com.geeksville.mesh.google.wear"
            } else {
                applicationId = "com.geeksville.mesh.fdroid.wear"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug") // For prototype
        }
    }
}

dependencies {
    implementation(projects.core.ble)
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.di)
    implementation(projects.core.domain)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.prefs)
    implementation(projects.core.proto)
    implementation(projects.core.resources)
    implementation(projects.core.ui)
    implementation(projects.core.service)
    implementation(projects.feature.messaging)
    implementation(projects.feature.connections)
    implementation(projects.feature.node)

    // WearOS specific
    implementation(libs.androidx.wear)
    implementation(libs.androidx.wear.compose.foundation)
    implementation(libs.androidx.wear.compose.material3)
    implementation(libs.androidx.wear.compose.navigation)
    
    // Core Android/Compose
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.jetbrains.lifecycle.viewmodel.compose)
    implementation(libs.jetbrains.lifecycle.runtime.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.annotations)
    implementation(libs.kermit)
}
