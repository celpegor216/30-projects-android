package fastcampus.aop.pjt26_sns.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import fastcampus.aop.pjt26_sns.DBKey.Companion.DB_ARTICLES
import fastcampus.aop.pjt26_sns.databinding.ActivityAddArticleBinding
import fastcampus.aop.pjt26_sns.gallery.GalleryActivity
import fastcampus.aop.pjt26_sns.photo.CameraActivity
import fastcampus.aop.pjt26_sns.photo.ImageListActivity.Companion.URI_LIST_KEY
import fastcampus.aop.pjt26_sns.photo.PhotoListAdapter
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AddArticleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddArticleBinding

    private var imageUriList: ArrayList<Uri> = arrayListOf()
    private val photoListAdapter = PhotoListAdapter { uri -> removePhoto(uri) }
    private fun removePhoto(uri: Uri) {
        imageUriList.remove(uri)
        photoListAdapter.setPhotoList(imageUriList)
    }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }
    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        photoRecyclerView.adapter = photoListAdapter

        imageAddButton.setOnClickListener {
            showPictureUploadDialog()
        }

        submitButton.setOnClickListener {
            showProgress()

            val userId = auth.currentUser?.uid.orEmpty()
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            // 이미지가 첨부되어 있으면 업로드
            if (imageUriList.isNotEmpty()) {
                lifecycleScope.launch {
                    val results = uploadPhoto(imageUriList)
                    afterUploadPhoto(results, title, content, userId)
                }
            } else {
                uploadArticle(userId, title, content, listOf())
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
                    return@async storage.reference.child("article/photo").child(fileName)
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
                    userId
                )
            }

            errorResults.isNotEmpty() && successResults.isEmpty() -> {
                uploadError()
            }

            else -> {
                uploadArticle(userId, title, content, successResults)
            }
        }
    }

    private fun uploadArticle(
        userId: String,
        title: String,
        content: String,
        imageUrlList: List<String>
    ) {
        val model =
            ArticleModel(userId, title, System.currentTimeMillis(), "$content 원", imageUrlList)
        articleDB.push().setValue(model)

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
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
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
                    val uriList = intent.getParcelableArrayListExtra<Uri>(URI_LIST_KEY)
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
        userId: String
    ) {
        AlertDialog.Builder(this)
            .setTitle("일부 사진 첨부 실패")
            .setMessage("이하의 사진 첨부에 실패했습니다.\n"
                    + errorResults.map { (uri, _) -> "$uri\n" }
                    + "게시글을 등록하시겠습니까?")
            .setPositiveButton("업로드") { _, _ ->
                uploadArticle(userId, title, content, successResults)
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
    }
}