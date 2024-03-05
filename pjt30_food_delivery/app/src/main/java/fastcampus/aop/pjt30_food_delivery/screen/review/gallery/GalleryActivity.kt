package fastcampus.aop.pjt30_food_delivery.screen.review.gallery

import android.app.Activity
import android.content.Intent
import androidx.core.view.isGone
import androidx.core.view.isVisible
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityGalleryBinding
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseActivity
import fastcampus.aop.pjt30_food_delivery.screen.review.photo.ImagePreviewListActivity.Companion.KEY_URI_LIST
import fastcampus.aop.pjt30_food_delivery.widget.adapter.GalleryPhotoListAdapter
import fastcampus.aop.pjt30_food_delivery.widget.adapter.GridDividerDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class GalleryActivity : BaseActivity<GalleryViewModel, ActivityGalleryBinding>() {

    override val viewModel by viewModel<GalleryViewModel>()

    override fun getViewBinding() = ActivityGalleryBinding.inflate(layoutInflater)

    private val adapter = GalleryPhotoListAdapter {
        viewModel.selectPhoto(it)
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            GridDividerDecoration(
                this@GalleryActivity,
                R.drawable.background_frame_gallery
            )
        )
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhotos()
        }
    }

    override fun observeData() = viewModel.galleryStateLiveData.observe(this) {
        when (it) {
            is GalleryState.Loading -> handleLoadingState()
            is GalleryState.Success -> handleSuccessState(it)
            is GalleryState.Confirm -> handleConfirmState(it)
            else -> Unit
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isVisible = true
        recyclerView.isGone = true
    }

    private fun handleSuccessState(state: GalleryState.Success) = with(binding) {
        progressBar.isGone = true
        recyclerView.isVisible = true

        adapter.setGalleryPhotoList(state.photoList)
    }

    private fun handleConfirmState(state: GalleryState.Confirm) {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(KEY_URI_LIST, ArrayList(state.photoList.map { it.uri }))
        })
        finish()
    }

    companion object {
        fun newIntent(activity: Activity) = Intent(activity, GalleryActivity::class.java)
    }
}