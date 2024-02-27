package fastcampus.aop.pjt30_food_delivery.data.repository.map

import android.util.Log
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.network.MapApiService
import fastcampus.aop.pjt30_food_delivery.data.response.address.AddressInfo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultMapRepository(
    private val mapApiService: MapApiService,
    private val ioDispatcher: CoroutineDispatcher
): MapRepository {

    override suspend fun getReverseGeoInformation(locationLatLngEntity: LocationLatLngEntity): AddressInfo? = withContext(ioDispatcher) {
        val response = mapApiService.getReverseGeoCode(
            lat = locationLatLngEntity.latitude,
            lon = locationLatLngEntity.longitude
        )

        if (response.isSuccessful) {
            response.body()?.addressInfo
        } else {
            null
        }
    }
}