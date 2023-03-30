package co.develhope.meteoapp.model

// Define an enum class to represent weather descriptions
enum class WeatherDescription {
    CLEAR_SKY, PARTLY_CLOUDY, CLOUDY, FOG,
    LIGHT_RAIN, MODERATE_RAIN, HEAVY_RAIN, LIGHT_SHOWER, HEAVY_SHOWER,
    FREEZING_RAIN, LIGHT_SNOW, MODERATE_SNOW, HEAVY_SNOW, SLEET, THUNDERSTORM,
    UNKNOWN
}

// Define an extension function for the Int class to get the corresponding weather description
fun Int.getWeatherDescription(): WeatherDescription {
    // Use a when expression to determine the weather description based on the input integer
    return when (this) {
        0, 1, 2, 3      -> WeatherDescription.PARTLY_CLOUDY
        45, 48          -> WeatherDescription.FOG
        51, 53, 55      -> WeatherDescription.CLOUDY
        56, 57          -> WeatherDescription.LIGHT_RAIN
        61, 63, 65      -> WeatherDescription.MODERATE_RAIN
        66, 67          -> WeatherDescription.HEAVY_RAIN
        71, 73, 75      -> WeatherDescription.LIGHT_SNOW
        77              -> WeatherDescription.SLEET
        80, 81, 82      -> WeatherDescription.LIGHT_SHOWER
        85, 86          -> WeatherDescription.HEAVY_SHOWER
        95              -> WeatherDescription.THUNDERSTORM
        else            -> WeatherDescription.UNKNOWN
    }
}

enum class WindDirection {
    N, NE, E, SE, S, SW, W, NW
}

fun Int.getWindDirection(): WindDirection {
    val angle = Math.floorMod(this, 360)
    return when {
        angle < 23 || angle >= 338 -> WindDirection.N
        angle < 68 -> WindDirection.NE
        angle < 113 -> WindDirection.E
        angle < 158 -> WindDirection.SE
        angle < 203 -> WindDirection.S
        angle < 248 -> WindDirection.SW
        angle < 293 -> WindDirection.W
        else -> WindDirection.NW
    }
}

