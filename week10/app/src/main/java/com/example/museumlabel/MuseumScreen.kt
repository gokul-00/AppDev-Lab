package com.example.museumlabel

enum class MuseumScreen {
    Home,
    Camera;

    companion object {
        fun fromRoute(route: String?): MuseumScreen {
            return when (route?.substringBefore("/")) {
                Home.name -> Home
                Camera.name -> Camera
                null -> Home
                else -> throw IllegalArgumentException("Invalid value: $route")
            }
        }
    }
}