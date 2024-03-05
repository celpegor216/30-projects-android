package fastcampus.aop.pjt30_food_delivery.data.repository.gallery

import fastcampus.aop.pjt30_food_delivery.data.entity.GalleryPhoto

interface GalleryPhotoRepository {

    suspend fun getAllPhotos(): MutableList<GalleryPhoto>
}