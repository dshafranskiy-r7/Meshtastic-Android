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
package org.meshtastic.wear

import android.app.Application
import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.meshtastic.core.common.ContextServices
import org.meshtastic.core.database.DatabaseManager
import org.meshtastic.core.repository.MeshPrefs
import org.meshtastic.wear.di.WearKoinModule
import org.meshtastic.wear.di.module

class WearApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        ContextServices.app = this

        startKoin {
            androidContext(this@WearApplication)
            modules(WearKoinModule().module())
        }

        // Initialize DatabaseManager asynchronously
        applicationScope.launch {
            val dbManager: DatabaseManager = get()
            val meshPrefs: MeshPrefs = get()
            dbManager.init(meshPrefs.deviceAddress.value)
        }
    }

    override fun onTerminate() {
        get<DatabaseManager>().close()
        applicationScope.cancel()
        super.onTerminate()
        org.koin.core.context.stopKoin()
    }
}
