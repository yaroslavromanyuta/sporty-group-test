package com.sportygroup.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.sportygroup.weatherapp.core.designsystem.theme.SkyTheme
import com.sportygroup.weatherapp.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val systemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(systemDark) }
            SkyTheme(darkTheme = darkTheme) {
                AppNavHost(
                    isDarkTheme = darkTheme,
                    onToggleTheme = { darkTheme = !darkTheme },
                )
            }
        }
    }
}