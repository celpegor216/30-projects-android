package fastcampus.aop.pjt30_food_delivery.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@androidx.room.Entity
@Parcelize
data class LocationLatLngEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = -1,
    val latitude: Double,
    val longitude: Double
): Entity, Parcelable
