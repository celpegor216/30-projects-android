package fastcampus.aop.pjt30_food_delivery.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity

@Dao
interface RestaurantDao {

    @Query("SELECT * FROM RestaurantEntity WHERE restaurantTitle=:title")
    suspend fun get(title: String): RestaurantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurantEntity: RestaurantEntity)

    @Query("DELETE FROM RestaurantEntity WHERE restaurantTitle=:title")
    suspend fun delete(title: String)
}