package co.develhope.meteoapp.model

data class CurrentWeather(
    var temperature     : Double,
    var windspeed       : Double,
    var winddirection   : Int,
    var weathercode     : Int,
    var time            : String
){
    val weatherDescription: WeatherDescription
        get() = weathercode.getWeatherDescription()
}