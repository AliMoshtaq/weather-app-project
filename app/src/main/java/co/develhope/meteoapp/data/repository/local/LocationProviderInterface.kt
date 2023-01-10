package co.develhope.meteoapp.data.repository.local

import co.develhope.meteoapp.data.dataModel.LocationData
import co.develhope.meteoapp.utils.RequestCompleteListener

interface LocationProviderInterface {
    fun getUserCurrentLocation(callback: RequestCompleteListener<LocationData>)
}