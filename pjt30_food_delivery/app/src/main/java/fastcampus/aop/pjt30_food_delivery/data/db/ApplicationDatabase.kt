package fastcampus.aop.pjt30_food_delivery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.pjt30_food_delivery.data.db.dao.FoodMenuBasketDao
import fastcampus.aop.pjt30_food_delivery.data.db.dao.LocationDao
import fastcampus.aop.pjt30_food_delivery.data.db.dao.RestaurantDao
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity

@Database(
    entities = [LocationLatLngEntity::class, RestaurantEntity::class, RestaurantFoodEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun LocationDao(): LocationDao

    abstract fun RestaurantDao(): RestaurantDao

    abstract fun FoodMenuBasketDao(): FoodMenuBasketDao

    companion object {
        const val DB_NAME = "ApplicationDatabase.db"
    }
}