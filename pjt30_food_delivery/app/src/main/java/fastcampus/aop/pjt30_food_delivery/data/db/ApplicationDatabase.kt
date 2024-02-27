package fastcampus.aop.pjt30_food_delivery.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.pjt30_food_delivery.data.db.dao.LocationDao
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity

@Database(
    entities = [LocationLatLngEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ApplicationDatabase: RoomDatabase() {

    abstract fun LocationDao(): LocationDao

    companion object {
        const val DB_NAME = "ApplicationDatabase.db"
    }
}