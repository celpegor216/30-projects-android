package fastcampus.aop.pjt30_food_delivery.screen.review.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt30_food_delivery.data.entity.GalleryPhoto
import fastcampus.aop.pjt30_food_delivery.data.repository.gallery.GalleryPhotoRepository
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GalleryViewModel(
    private val galleryPhotoRepository: GalleryPhotoRepository
): BaseViewModel() {

    private lateinit var photoList: MutableList<GalleryPhoto>

    private val _galleryStateLiveData = MutableLiveData<GalleryState>(GalleryState.Uninitialized)
    val galleryStateLiveData: LiveData<GalleryState> = _galleryStateLiveData

    override fun fetchData(): Job = viewModelScope.launch {
        setState(GalleryState.Loading)
        photoList = galleryPhotoRepository.getAllPhotos()
        setState(GalleryState.Success(photoList))
    }

    fun selectPhoto(galleryPhoto: GalleryPhoto) {
        val findGalleryPhoto = photoList.find { it.id == galleryPhoto.id }
        findGalleryPhoto?.let { photo ->
            photoList[photoList.indexOf(photo)] = photo.copy(isSelected = photo.isSelected.not())

            setState(GalleryState.Success(photoList))
        }
    }

    fun confirmCheckedPhotos() {
        setState(GalleryState.Loading)
        setState(
            GalleryState.Confirm(
            photoList = photoList.filter { it.isSelected }
        ))
    }

    private fun setState(state: GalleryState) {
        _galleryStateLiveData.postValue(state)
    }
}