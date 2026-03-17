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

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Card
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import org.koin.compose.viewmodel.koinViewModel
import org.meshtastic.core.model.DataPacket
import org.meshtastic.feature.connections.AndroidScannerViewModel
import org.meshtastic.feature.messaging.MessageViewModel
import org.meshtastic.feature.node.list.NodeListViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MeshtasticWearApp()
        }
    }
}

@Composable
fun MeshtasticWearApp() {
    val navController = rememberSwipeDismissableNavController()
    MaterialTheme {
        AppScaffold {
            SwipeDismissableNavHost(
                navController = navController,
                startDestination = "node_list"
            ) {
                composable("node_list") {
                    NodeListScreen(
                        onNodeClick = { nodeNum ->
                            val contactKey = "0${DataPacket.nodeNumToDefaultId(nodeNum)}"
                            navController.navigate("messages/$contactKey")
                        },
                        onConnectClick = {
                            navController.navigate("connections")
                        }
                    )
                }
                composable("messages/{contactKey}") { backStackEntry ->
                    val contactKey = backStackEntry.arguments?.getString("contactKey") ?: ""
                    MessagingScreen(contactKey = contactKey)
                }
                composable("connections") {
                    ConnectionsScreen()
                }
            }
        }
    }
}

@Composable
fun NodeListScreen(
    onNodeClick: (Int) -> Unit,
    onConnectClick: () -> Unit,
    viewModel: NodeListViewModel = koinViewModel()
) {
    val nodeList by viewModel.nodeList.collectAsStateWithLifecycle()
    
    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Button(
                onClick = onConnectClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connect Device")
            }
        }
        items(nodeList) { node ->
            Button(
                onClick = { onNodeClick(node.num) },
                modifier = Modifier.fillMaxWidth()
            ) {
                val displayName = node.user.short_name.takeIf { !it.isNullOrBlank() } 
                    ?: node.user.long_name.takeIf { !it.isNullOrBlank() }
                    ?: node.num.toString()
                Text(displayName)
            }
        }
    }
}

@Composable
fun MessagingScreen(
    contactKey: String,
    viewModel: MessageViewModel = koinViewModel()
) {
    val messages by viewModel.getMessagesFlow(contactKey).collectAsStateWithLifecycle(initialValue = emptyList())
    
    // Ensure the ViewModel is initialized with the contactKey
    LaunchedEffect(contactKey) {
        viewModel.setContactKey(contactKey)
    }

    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Text(
                text = "Chat: $contactKey",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        items(messages) { message ->
            Card(
                onClick = { /* Reply? */ },
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(
                    text = "${message.node.user.short_name ?: message.node.num}:",
                    style = MaterialTheme.typography.labelSmall
                )
                Text(text = message.text)
            }
        }
        if (messages.isEmpty()) {
            item {
                Text("No messages yet")
            }
        }
    }
}

@Composable
fun ConnectionsScreen(
    viewModel: AndroidScannerViewModel = koinViewModel()
) {
    val bleDevices by viewModel.bleDevicesForUi.collectAsStateWithLifecycle()
    val isScanning by viewModel.isBleScanning.collectAsStateWithLifecycle()

    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Button(
                onClick = {
                    if (isScanning) viewModel.stopBleScan() else viewModel.startBleScan()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isScanning) "Stop Scan" else "Scan Bluetooth")
            }
        }
        items(bleDevices) { device ->
            Button(
                onClick = { viewModel.onSelected(device) },
                modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
            ) {
                Text(device.name)
            }
        }
        if (bleDevices.isEmpty() && !isScanning) {
            item {
                Text("No devices found", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
