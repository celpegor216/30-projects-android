package fastcampus.aop.pjt24_online_store.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fastcampus.aop.pjt24_online_store.data.db.dao.ProductDao
import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity
import fastcampus.aop.pjt24_online_store.utility.DateConverter

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ProductDatabase: RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        const val DB_NAME = "ProductDatabase.db"
    }
}