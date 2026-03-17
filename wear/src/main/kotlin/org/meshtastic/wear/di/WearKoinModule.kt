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
package org.meshtastic.wear.di

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ProcessLifecycleOwner
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single
import org.meshtastic.core.ble.di.CoreBleAndroidModule
import org.meshtastic.core.ble.di.CoreBleModule
import org.meshtastic.core.common.BuildConfigProvider
import org.meshtastic.core.common.di.CoreCommonModule
import org.meshtastic.core.data.di.CoreDataAndroidModule
import org.meshtastic.core.data.di.CoreDataModule
import org.meshtastic.core.database.di.CoreDatabaseAndroidModule
import org.meshtastic.core.database.di.CoreDatabaseModule
import org.meshtastic.core.datastore.di.CoreDatastoreAndroidModule
import org.meshtastic.core.datastore.di.CoreDatastoreModule
import org.meshtastic.core.network.di.CoreNetworkModule
import org.meshtastic.core.prefs.di.CorePrefsAndroidModule
import org.meshtastic.core.prefs.di.CorePrefsModule
import org.meshtastic.core.service.di.CoreServiceAndroidModule
import org.meshtastic.core.service.di.CoreServiceModule
import org.meshtastic.core.ui.di.CoreUiModule
import org.meshtastic.feature.connections.di.FeatureConnectionsModule
import org.meshtastic.feature.messaging.di.FeatureMessagingModule
import org.meshtastic.feature.node.di.FeatureNodeModule
import org.meshtastic.wear.BuildConfig

@Module(
    includes =
    [
        CoreCommonModule::class,
        CoreBleModule::class,
        CoreBleAndroidModule::class,
        CoreDataModule::class,
        CoreDataAndroidModule::class,
        org.meshtastic.core.domain.di.CoreDomainModule::class,
        CoreDatabaseModule::class,
        CoreDatabaseAndroidModule::class,
        org.meshtastic.core.repository.di.CoreRepositoryModule::class,
        CoreDatastoreModule::class,
        CoreDatastoreAndroidModule::class,
        CorePrefsModule::class,
        CorePrefsAndroidModule::class,
        CoreServiceModule::class,
        CoreServiceAndroidModule::class,
        CoreNetworkModule::class,
        CoreUiModule::class,
        FeatureNodeModule::class,
        FeatureMessagingModule::class,
        FeatureConnectionsModule::class,
    ],
)
class WearKoinModule {
    @Single
    @Named("ProcessLifecycle")
    fun provideProcessLifecycle(): Lifecycle = ProcessLifecycleOwner.get().lifecycle

    @Single
    fun provideCoroutineDispatchers(): org.meshtastic.core.di.CoroutineDispatchers =
        org.meshtastic.core.di.CoroutineDispatchers(
            io = kotlinx.coroutines.Dispatchers.IO,
            main = kotlinx.coroutines.Dispatchers.Main,
            default = kotlinx.coroutines.Dispatchers.Default,
        )

    @Single
    fun provideBuildConfigProvider(): BuildConfigProvider = object : BuildConfigProvider {
        override val isDebug: Boolean = BuildConfig.DEBUG
        override val applicationId: String = BuildConfig.APPLICATION_ID
        override val versionCode: Int = BuildConfig.VERSION_CODE
        override val versionName: String = BuildConfig.VERSION_NAME
        override val absoluteMinFwVersion: String = "2.3.15"
        override val minFwVersion: String = "2.5.14"
    }
}
