package co.develhope.meteoapp.interfaces

interface RequestCompleteListener<T> {
    fun onRequestCompleted(data: T)
    fun onRequestFailed(errorMessage: String)
}