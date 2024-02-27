package fastcampus.aop.pjt30_food_delivery.screen.mylocation

import androidx.annotation.StringRes
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity

sealed class MyLocationState {

    object Uninitialized: MyLocationState()

    object Loading: MyLocationState()

    data class Success(
        val mapSearchInfo: MapSearchInfoEntity
    ): MyLocationState()

    data class Confirm(
        val mapSearchInfo: MapSearchInfoEntity
    ): MyLocationState(

    )
    data class Error(
        @StringRes val messageId: Int
    ): MyLocationState()
}