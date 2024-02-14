package fastcampus.aop.pjt24_online_store.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fastcampus.aop.pjt24_online_store.data.entity.product.ProductEntity

@Dao
interface ProductDao {

    @Query("SELECT * FROM ProductEntity WHERE id=:id")
    suspend fun getById(id: Long): ProductEntity?

    @Query("SELECT * FROM ProductEntity")
    suspend fun getAll(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productEntity: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productList: List<ProductEntity>)

    @Query("DELETE FROM ProductEntity WHERE id=:id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM ProductEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(productEntity: ProductEntity)
}