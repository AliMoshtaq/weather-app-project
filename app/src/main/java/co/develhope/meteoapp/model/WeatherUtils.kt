package co.develhope.meteoapp.model

enum class WeatherDescription {
    CLEAR_SKY, PARTLY_CLOUDY, FOGGY,
    DENSE_INTENSITY, RAINY, HEAVY_INTENSITY,
    FREEZING_RAIN, SNOW_FALL, SNOW_GRAINS, RAIN_SHOWERS,
    HEAVY_SNOW, THUNDERSTORM, HEAVY_HAIL
}

fun Int.getWeatherDescription(): WeatherDescription {
    return when (this) {
        0           -> WeatherDescription.CLEAR_SKY
        1,2,3       -> WeatherDescription.PARTLY_CLOUDY
        45, 48      -> WeatherDescription.FOGGY
        51, 53, 55  -> WeatherDescription.DENSE_INTENSITY
        56, 57      -> WeatherDescription.RAINY
        61, 63, 65  -> WeatherDescription.HEAVY_INTENSITY
        66, 67      -> WeatherDescription.FREEZING_RAIN
        71, 73, 75  -> WeatherDescription.SNOW_FALL
        77          -> WeatherDescription.SNOW_GRAINS
        80, 81, 82  -> WeatherDescription.RAIN_SHOWERS
        85, 86      -> WeatherDescription.HEAVY_SNOW
        95          -> WeatherDescription.THUNDERSTORM
        else        -> WeatherDescription.HEAVY_HAIL
    }
}

enum class WindDirection {
    N,NE, E, SE, S, SW, W, NW
}

fun Int.getWindDirection(): WindDirection {
    return when (this){
        10, in 346..360 -> WindDirection.N
        in 20..75 -> WindDirection.NE
        in 76..125 -> WindDirection.E
        in 126..165 -> WindDirection.SE
        in 166..215 -> WindDirection.S
        in 216..255 -> WindDirection.SW
        in 256 .. 295 -> WindDirection.W
        in 296 .. 345 -> WindDirection.NW
        else -> WindDirection.N
    }
}