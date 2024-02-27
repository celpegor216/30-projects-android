package fastcampus.aop.pjt30_food_delivery.data.repository.user

import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity

interface UserRepository {

    suspend fun getUserLocation(): LocationLatLngEntity?

    suspend fun insertUserLocation(locationLatLngEntity: LocationLatLngEntity)
}