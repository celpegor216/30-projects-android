package fastcampus.aop.pjt30_food_delivery.screen.review

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import fastcampus.aop.pjt30_food_delivery.data.entity.ReviewEntity
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityAddReviewBinding
import fastcampus.aop.pjt30_food_delivery.screen.review.gallery.GalleryActivity
import fastcampus.aop.pjt30_food_delivery.screen.review.photo.CameraActivity
import fastcampus.aop.pjt30_food_delivery.screen.review.photo.ImagePreviewListActivity.Companion.KEY_URI_LIST
import fastcampus.aop.pjt30_food_delivery.widget.adapter.PhotoListAdapter
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class AddReviewActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAddReviewBinding

    private val auth by inject<FirebaseAuth>()
    private val storage by inject<FirebaseStorage>()
    private val firestore by inject<FirebaseFirestore>()

    private val restaurantTitle by lazy {
        intent.getStringExtra(KEY_RESTAURANT_TITLE)!!
    }
    private val orderId by lazy {
        intent.getStringExtra(KEY_ORDER_ID)!!
    }

    private var imageUriList: ArrayList<Uri> = arrayListOf()
    private val photoListAdapter = PhotoListAdapter { uri -> removePhoto(uri) }
    private fun removePhoto(uri: Uri) {
        imageUriList.remove(uri)
        photoListAdapter.setPhotoList(imageUriList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        titleTextView.text = restaurantTitle

        toolbar.setNavigationOnClickListener {
            finish()
        }

        photoRecyclerView.adapter = photoListAdapter

        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            showProgress()

            val userId = auth.currentUser?.uid.orEmpty()
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val rating = ratingBar.rating

            // 이미지가 첨부되어 있으면 업로드
            if (imageUriList.isNotEmpty()) {
                lifecycleScope.launch {
                    val results = uploadPhoto(imageUriList)
                    afterUploadPhoto(results, title, content, rating, userId)
                }
            } else {
                uploadReview(userId, title, content, rating, listOf())
            }
        }
    }

    private fun showPictureUploadDialog() {
        AlertDialog.Builder(this)
            .setTitle("사진 첨부")
            .setMessage("사진을 첨부할 방식을 선택해주세요.")
            .setPositiveButton("카메라") { _, _ ->
                checkExternalStoragePermission {
                    startCameraScreen()
                }
            }
            .setNegativeButton("갤러리") { _, _ ->
                checkExternalStoragePermission {
                    startGalleryScreen()
                }
            }
            .create()
            .show()
    }

    private fun checkExternalStoragePermission(uploadAction: () -> Unit) {
        when {
            ContextCompat.checkSelfPermission(
                this,
                REQUIRED_PERMISSIONS
            ) == PackageManager.PERMISSION_GRANTED -> {
                uploadAction()
            }

            shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS) -> {
                showPermissionContextPopup()
            }

            else -> {
                requestPermissions(arrayOf(REQUIRED_PERMISSIONS), PERMISSION_REQUEST_CODE)
            }
        }
    }

    private suspend fun uploadPhoto(uriList: List<Uri>) = withContext(Dispatchers.IO) {
        val uploadDeferred: List<Deferred<Any>> = uriList.mapIndexed { index, uri ->
            // async는 launch와 달리 값을 반환
            lifecycleScope.async {
                try {
                    val fileName = "image_$index.png"
                    return@async storage.reference.child("reviews/photo").child(fileName)
                        .putFile(uri)
                        .await()
                        .storage.downloadUrl
                        .await()
                        .toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@async Pair(uri, e)
                }
            }
        }
        return@withContext uploadDeferred.awaitAll()
    }

    private fun afterUploadPhoto(
        results: List<Any>,
        title: String,
        content: String,
        rating: Float,
        userId: String
    ) {
        val errorResults = results.filterIsInstance<Pair<Uri, Exception>>()
        val successResults = results.filterIsInstance<String>()

        when {
            errorResults.isNotEmpty() && successResults.isNotEmpty() -> {
                photoUploadErrorButContinueDialog(
                    errorResults,
                    successResults,
                    title,
                    content,
                    rating,
                    userId
                )
            }

            errorResults.isNotEmpty() && successResults.isEmpty() -> {
                uploadError()
            }

            else -> {
                uploadReview(userId, title, content, rating, successResults)
            }
        }
    }

    private fun uploadReview(
        userId: String,
        title: String,
        content: String,
        rating: Float,
        imageUrlList: List<String>
    ) {
        val review =
            ReviewEntity(
                userId = userId,
                createdAt = System.currentTimeMillis(),
                title = title,
                content = content,
                rating = rating,
                imageUrlList = imageUrlList,
                orderId = orderId,
                restaurantTitle = restaurantTitle
            )

        firestore.collection(COLLECTION_REVIEW).add(review)

        Toast.makeText(this, "리뷰가 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()

        hideProgress()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGalleryScreen()
            } else {
                Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startGalleryScreen() {
        startActivityForResult(
            GalleryActivity.newIntent(this),
            GALLERY_REQUEST_CODE
        )
    }

    private fun startCameraScreen() {
        startActivityForResult(
            CameraActivity.newIntent(this),
            CAMERA_REQUEST_CODE
        )
    }

    private fun showProgress() = with(binding) {
        progressBar.isVisible = true
    }

    private fun hideProgress() = with(binding) {
        progressBar.isVisible = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(KEY_URI_LIST)
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        photoListAdapter.setPhotoList(imageUriList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            CAMERA_REQUEST_CODE -> {
                data?.let { intent ->
                    val uriList = intent.getParcelableArrayListExtra<Uri>(KEY_URI_LIST)
                    uriList?.let { list ->
                        imageUriList.addAll(list)
                        photoListAdapter.setPhotoList(imageUriList)
                    }
                } ?: kotlin.run {
                    Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                Toast.makeText(this, "사진을 가져오지 못했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("사진을 가져오기 위해 권한이 필요합니다.")
            .setPositiveButton("확인") { _, _ ->
                requestPermissions(arrayOf(REQUIRED_PERMISSIONS), PERMISSION_REQUEST_CODE)
            }
    }

    private fun photoUploadErrorButContinueDialog(
        errorResults: List<Pair<Uri, Exception>>,
        successResults: List<String>,
        title: String,
        content: String,
        rating: Float,
        userId: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("일부 사진 첨부 실패")
            .setMessage("이하의 사진 첨부에 실패했습니다.\n"
                    + errorResults.map { (uri, _) -> "$uri\n" }
                    + "게시글을 등록하시겠습니까?")
            .setPositiveButton("업로드") { _, _ ->
                uploadReview(userId, title, content, rating, successResults)
            }
            .create()
            .show()
    }

    private fun uploadError() {
        Toast.makeText(this, "사진 첨부에 실패했습니다.", Toast.LENGTH_SHORT).show()

        hideProgress()
        finish()
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE

        const val PERMISSION_REQUEST_CODE = 1000
        const val GALLERY_REQUEST_CODE = 1001
        const val CAMERA_REQUEST_CODE = 1002

        private const val KEY_RESTAURANT_TITLE = "restaurantTitle"
        private const val KEY_ORDER_ID = "orderId"
        const val COLLECTION_REVIEW = "review"

        fun newIntent(
            context: Context,
            orderId: String,
            restaurantTitle: String,
        ) = Intent(context, AddReviewActivity::class.java).apply {
            putExtra(KEY_ORDER_ID, orderId)
            putExtra(KEY_RESTAURANT_TITLE, restaurantTitle)
        }
    }
}