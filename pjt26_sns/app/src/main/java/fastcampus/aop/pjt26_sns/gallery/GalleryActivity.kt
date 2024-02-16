package fastcampus.aop.pjt26_sns.gallery

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import fastcampus.aop.pjt26_sns.R
import fastcampus.aop.pjt26_sns.databinding.ActivityGalleryBinding
import fastcampus.aop.pjt26_sns.photo.ImageListActivity.Companion.URI_LIST_KEY

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val adapter = GalleryPhotoListAdapter {
        viewModel.selectPhoto(it)
    }

    private val viewModel by viewModels<GalleryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.fetchData()
        initViews()
        observeState()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(GridDividerDecoration(this@GalleryActivity, R.drawable.background_frame_gallery))
        confirmButton.setOnClickListener {
            viewModel.confirmCheckedPhotos()
        }
    }

    private fun observeState() = viewModel.galleryStateLiveData.observe(this) {
        when(it) {
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
            putExtra(URI_LIST_KEY, ArrayList(state.photoList.map { it.uri }))
        })
        finish()
    }

    companion object {
        fun newIntent(activity: Activity) = Intent(activity, GalleryActivity::class.java)
    }
}