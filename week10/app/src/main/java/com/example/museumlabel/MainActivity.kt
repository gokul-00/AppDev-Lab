package com.example.museumlabel

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.museumlabel.ui.theme.MuseumLabelTheme
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.museumlabel.ui.camera.CameraScreen
import com.example.museumlabel.ui.home.HomeScreen
import com.example.museumlabel.MuseumScreen.Home
import com.example.museumlabel.MuseumScreen.Camera
import java.util.*

@androidx.camera.core.ExperimentalGetImage
class MainActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("MainActivity", "This Language is not supported")
                }
            } else {
                Log.e("MainActivity", "Initialization Failed! Code: $status")
            }
        }

        setContent {
            val navController = rememberNavController()
            val backstackEntry = navController.currentBackStackEntryAsState()
            val currentScreen = MuseumScreen.fromRoute(backstackEntry.value?.destination?.route)
            MuseumLabelTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = currentScreen.name,
                                    style = MaterialTheme.typography.h6
                                )
                            },
                            navigationIcon =
                            if (currentScreen != Home) {
                                {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(Icons.Default.ArrowBack, "Back")
                                    }
                                }
                            } else null,
                        )
                    },
                    floatingActionButton = {
                        if (currentScreen.name == Home.name) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate(Camera.name)
                                }
                            ) {
                                Icon(
                                    if (currentScreen.name == Home.name) Icons.Default.Search else Icons.Default.ArrowBack,
                                    "Camera Icon"
                                )
                            }
                        }
                    },
                ) { innerPadding ->
                    MuseumNavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        sayText = {
                            tts.setPitch(1.0f)
                            tts.setSpeechRate(1.0f)
                            tts.speak(it, TextToSpeech.QUEUE_FLUSH, null, null)
                        }
                    )
                }
            }
        }
    }
}

@Composable
@androidx.camera.core.ExperimentalGetImage
fun MuseumNavHost(navController: NavHostController, modifier: Modifier = Modifier, sayText: (String) -> Unit = {}) {
    val initialText = "Hello There!"
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = "${Home.name}/{text}",
        modifier = modifier
    ) {
        composable(
            route = "${Home.name}/{text}",
            arguments = listOf(
                navArgument("text") { type = NavType.StringType }
            )
        ) { entry ->
            val text = entry.arguments?.getString("text") ?: initialText
            HomeScreen(text) { textToRead ->
                sayText(textToRead)
            }
        }
        composable(
            Camera.name
        ) {
            CameraScreen { text ->
                Toast.makeText(
                    context,
                    "Text: $text",
                    Toast.LENGTH_SHORT
                ).show()
                navController.navigate("${Home.name}/$text") {
                    popUpTo(Camera.name)
                    popUpTo(Home.name) {inclusive = true}
                }
            }
        }
    }
}
