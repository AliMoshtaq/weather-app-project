package co.develhope.meteoapp.data.dataModel

enum class Weather {
    CLEAR_SKY, PARTLY_CLOUDY, FOGGY,
    DENSE_INTENSITY, RAINY, HEAVY_INTENSITY,
    FREEZING_RAIN, SNOW_FALL, SNOW_GRAINS, RAIN_SHOWERS,
    HEAVY_SNOW, THUNDERSTORM, HEAVY_HAIL
}

fun Int.getWeatherDescription(): Weather {
    return when (this) {
        0           -> Weather.CLEAR_SKY
        1,2,3       -> Weather.PARTLY_CLOUDY
        45, 48      -> Weather.FOGGY
        51, 53, 55  -> Weather.DENSE_INTENSITY
        56, 57      -> Weather.RAINY
        61, 63, 65  -> Weather.HEAVY_INTENSITY
        66, 67      -> Weather.FREEZING_RAIN
        71, 73, 75  -> Weather.SNOW_FALL
        77          -> Weather.SNOW_GRAINS
        80, 81, 82  -> Weather.RAIN_SHOWERS
        85, 86      -> Weather.HEAVY_SNOW
        95          -> Weather.THUNDERSTORM
        else        -> Weather.HEAVY_HAIL
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