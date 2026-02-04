/*
 * Copyright (c) 2025-2026 Meshtastic LLC
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

package com.geeksville.mesh.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.items
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity for Meshtastic Wear OS app
 * Supports standalone mode (direct Bluetooth connection) and companion mode (phone sync)
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    MaterialTheme {
        val navController = rememberSwipeDismissableNavController()
        
        Scaffold(
            timeText = {
                TimeText()
            },
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            }
        ) {
            WearNavHost(navController = navController)
        }
    }
}

@Composable
fun WearNavHost(navController: NavHostController) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable("messages") {
            MessagesPlaceholderScreen()
        }
        composable("nodes") {
            NodesPlaceholderScreen()
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = "Meshtastic",
                    style = MaterialTheme.typography.title1,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )
            }
            
            item {
                Chip(
                    onClick = { navController.navigate("messages") },
                    label = {
                        Text(
                            text = "Messages",
                            style = MaterialTheme.typography.button
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = ChipDefaults.primaryChipColors()
                )
            }
            
            item {
                Chip(
                    onClick = { navController.navigate("nodes") },
                    label = {
                        Text(
                            text = "Nodes",
                            style = MaterialTheme.typography.button
                        )
                    },
                    modifier = Modifier.fillMaxWidth(0.9f),
                    colors = ChipDefaults.primaryChipColors()
                )
            }
            
            item {
                Text(
                    text = "Standalone Mode",
                    style = MaterialTheme.typography.caption3,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun MessagesPlaceholderScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Messages",
            style = MaterialTheme.typography.title2,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = "Coming soon",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun NodesPlaceholderScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nodes",
            style = MaterialTheme.typography.title2,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = "Coming soon",
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

