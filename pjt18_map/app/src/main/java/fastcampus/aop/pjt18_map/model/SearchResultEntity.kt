package fastcampus.aop.pjt18_map.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchResultEntity(
    val fullAddress: String,
    val name: String,
    val locationLatLngEntity: LocationLatLngEntity
): Parcelable
