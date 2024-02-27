package fastcampus.aop.pjt30_food_delivery.data.repository.user

import fastcampus.aop.pjt30_food_delivery.data.db.dao.LocationDao
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import kotlinx.coroutines.CoroutineDispatcher

class DefaultUserRepository(
    private val locationDao: LocationDao,
    private val ioDispatcher: CoroutineDispatcher
): UserRepository {

    override suspend fun getUserLocation(): LocationLatLngEntity? = with(ioDispatcher) {
        locationDao.get(-1)
    }

    override suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity) {
        locationDao.insert(locationLatLngEntity)
    }
}