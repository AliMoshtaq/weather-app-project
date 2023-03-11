package co.develhope.meteoapp.network.interfaces

interface RequestCompleteListener<T> {
    fun onRequestCompleted(data: T)
    fun onRequestFailed(errorMessage: String)
}