package fastcampus.aop.pjt30_food_delivery.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.util.converter.RoomTypeConverters
import kotlinx.parcelize.Parcelize

@androidx.room.Entity
@TypeConverters(RoomTypeConverters::class)
@Parcelize
data class RestaurantEntity(
    override val id: Long,
    val restaurantInfoId: Long,
    val restaurantCategory: RestaurantCategory,
    @PrimaryKey val restaurantTitle: String,
    val restaurantImageUrl: String,
    val grade: Float,
    val reviewCount: Int,
    val deliveryTimeRange: Pair<Int, Int>,
    val deliveryTipRange: Pair<Int, Int>,
    val restaurantTelNumber: String?
) : Entity, Parcelable