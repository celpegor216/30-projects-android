package fastcampus.aop.pjt30_food_delivery.screen.review.gallery

import androidx.annotation.IdRes
import fastcampus.aop.pjt30_food_delivery.data.entity.GalleryPhoto

sealed class GalleryState {

    object Uninitialized: GalleryState()

    object Loading: GalleryState()

    data class Success(
        val photoList: List<GalleryPhoto>,
        @IdRes val toastId: Int? = null
    ): GalleryState()

    data class Confirm(
        val photoList: List<GalleryPhoto>
    ): GalleryState()
}