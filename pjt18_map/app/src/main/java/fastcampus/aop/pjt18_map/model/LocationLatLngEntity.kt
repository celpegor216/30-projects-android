package fastcampus.aop.pjt18_map.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LocationLatLngEntity(
    val lat: Float,
    val lng: Float
): Parcelable