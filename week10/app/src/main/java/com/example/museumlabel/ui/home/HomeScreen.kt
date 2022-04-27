package com.example.museumlabel.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

data class Location(val latitude: Double, val longitude: Double)

@Serializable
data class WorldStat (
    val data: List<RealLocation>? = null
)

@Serializable
data class RealLocation (
    val latitude: Double? = null,
    val longitude: Double? = null,
    val type: String? = null,
    val distance: Double? = null,
    val name: String? = null,
    val number: String? = null,
    val postalCode: String? = null,
    val street: String? = null,
    val confidence: Double? = null,
    val region: String? = null,
    val regionCode: String? = null,
    val county: String? = null,
    val locality: String? = null,
    val neighbourhood: String? = null,
    val country: String? = null,
    val countryCode: String? = null,
    val continent: String? = null,
    val label: String? = null
)

suspend fun getLocationFromLatitudeAndLongitude(location: Location): RealLocation? {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }
    val response = client.request("http://api.positionstack.com/v1/reverse?access_key=ccf2cecaed37f84aee1c17f238abb1c4&query=${location.latitude},${location.longitude}") {
        method = HttpMethod.Get
    }
    val worldStat: WorldStat = response.body()
    return worldStat.data?.get(0)
}

fun getLatitudeAndLongitudeFromString(locationString: String): Location {
    // sample string
    // Name
    // latitude: -33.8670522
    // longitude: 151.1957362
    return try {
        val location = locationString.split("\n")
        val latitude = location[1].replace(" ", "").split(":")[1].toDouble()
        val longitude = location[2].replace(" ", "").split(":")[1].toDouble()
        Location(latitude, longitude)
    } catch (e: Exception) {
        Location(0.0, 0.0)
    }

}

@Composable
fun HomeScreen(text: String, onButtonPress: (textToRead: String) -> Unit) {
    var locationString: String by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val location = getLatitudeAndLongitudeFromString(text)
        if (location.latitude != 0.0 && location.longitude != 0.0) {
            locationString = getLocationFromLatitudeAndLongitude(location)?.label.toString()
            if (locationString == "null") {
                locationString = "Couldn't find location"
            }
        } else {
            locationString = "Couldn't get latitude and longitude"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = text, fontSize = 20.sp)
        if (locationString.isNotEmpty()) {
            Text(text = locationString, fontSize = 20.sp)
        } else {
            CircularProgressIndicator()
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            var textToRead = text
            if (locationString.isNotEmpty()) {
                textToRead += " in " + locationString
            }
            onButtonPress(textToRead)
        }) {
            Text(text = "Read Out Text", fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen("Hello World") {}
}