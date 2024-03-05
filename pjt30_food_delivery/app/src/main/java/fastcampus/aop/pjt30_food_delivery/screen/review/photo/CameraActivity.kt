package fastcampus.aop.pjt30_food_delivery.screen.review.photo

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.impl.ImageOutputConfig
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityCameraBinding
import fastcampus.aop.pjt30_food_delivery.extension.load
import fastcampus.aop.pjt30_food_delivery.util.path.PathUtil
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var root: View? = null

    private val displayManager by lazy {
        getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }
    private var displayId: Int = -1
    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(p0: Int) {}

        override fun onDisplayRemoved(p0: Int) {}

        override fun onDisplayChanged(displayId: Int) {
            if (this@CameraActivity.displayId == displayId) {
                if (::imageCapture.isInitialized && root != null) {
                    imageCapture.targetRotation = root?.display?.rotation ?: ImageOutputConfig.INVALID_ROTATION
                }
            }
        }
    }

    private lateinit var cameraExecutor: ExecutorService
    private val cameraMainExecutor by lazy {
        ContextCompat.getMainExecutor(this)
    }
    private val cameraProviderFuture by lazy {
        ProcessCameraProvider.getInstance(this)
    }
    private var camera: Camera? = null
    private lateinit var imageCapture: ImageCapture
    private var isCapturing = false
    private var isFlashEnabled = false

    private val uriList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCameraBinding.inflate(layoutInflater)
        root = binding.root
        setContentView(root)

        if(allPermissionsGranted()) {
            startCamera(binding.viewFinder)
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera(binding.viewFinder)
            } else {
                Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera(viewFinder: PreviewView) {
        // 화면 회전 감지
        displayManager.registerDisplayListener(displayListener, null)

        // 카메라 호출
        cameraExecutor = Executors.newSingleThreadExecutor()
        viewFinder.postDelayed({
            displayId = viewFinder.display.displayId
            bindCameraUseCase()
        }, 10)
    }

    private fun bindCameraUseCase() = with(binding) {
        // 화면 방향 설정
        val rotation = viewFinder.display.rotation

        // 후면 카메라 지정
        val cameraSelector = CameraSelector.Builder().requireLensFacing(LENS_FACING).build()

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
                setTargetRotation(rotation)
            }.build()
            val imageCaptureBuilder = ImageCapture.Builder().apply {
                // 지연 최소화
                setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)

                // 자동 플래시
                setFlashMode(ImageCapture.FLASH_MODE_AUTO)

                // 미리보기와 동일한 비율, 방향 지정
                setTargetAspectRatio(AspectRatio.RATIO_4_3)
                setTargetRotation(rotation)
            }

            imageCapture = imageCaptureBuilder.build()

            try {
                // 기존에 바인딩 된 카메라 해제
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(
                    this@CameraActivity,
                    cameraSelector,
                    preview,
                    imageCapture
                )

                // 미리보기 지정
                preview.setSurfaceProvider(viewFinder.surfaceProvider)

                initFlashAndAddListener()
                bindZoomListener()
                bindCaptureListener()
                bindPreviewImageViewClickListener()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, cameraMainExecutor)
    }

    private fun initFlashAndAddListener() = with(binding) {
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        flashSwitch.isVisible = hasFlash
        if (hasFlash){
            flashSwitch.setOnCheckedChangeListener { _, isChecked ->
                isFlashEnabled = isChecked
            }
        } else {
            flashSwitch.setOnCheckedChangeListener(null)
        }
    }

    private fun bindZoomListener() = with(binding) {
        val listener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val currentZoomRatio = camera?.cameraInfo?.zoomState?.value?.zoomRatio ?: 1f

                val delta = detector.scaleFactor
                camera?.cameraControl?.setZoomRatio(currentZoomRatio * delta)

                return true
            }
        }

        val scaleGestureDetector = ScaleGestureDetector(this@CameraActivity, listener)
        viewFinder.setOnTouchListener { _, motionEvent ->
            scaleGestureDetector.onTouchEvent(motionEvent)
            return@setOnTouchListener true
        }
    }

    private fun bindCaptureListener() = with(binding) {
        captureButton.setOnClickListener {
            if (!isCapturing) {
                isCapturing = true
                captureCamera()
            }
        }
    }

    private var contentUri: Uri? = null

    private fun captureCamera() {
        if (::imageCapture.isInitialized.not()) return

        val photoFile = File(
            PathUtil.getOutputDirectory(this),
            SimpleDateFormat(
                FILENAME_FORMAT, Locale.KOREA
            ).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        flashLight(isFlashEnabled)

        imageCapture.takePicture(outputOptions, cameraExecutor, object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                contentUri = savedUri

                updateSavedImageContent()
            }

            override fun onError(exception: ImageCaptureException) {
                exception.printStackTrace()
                isCapturing = false
                flashLight(false)
            }
        })
    }

    private fun flashLight(light: Boolean) {
        val hasFlash = camera?.cameraInfo?.hasFlashUnit() ?: false
        if (hasFlash) {
            camera?.cameraControl?.enableTorch(light)
        }
    }

    private fun updateSavedImageContent() {
        contentUri?.let {
            isCapturing = try {
                val file = File(PathUtil.getPath(this, it) ?: throw FileNotFoundException())
                MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"), null)

                // 촬영한 사진을 버튼 왼쪽 미리보기 칸에 표시
                Handler(Looper.getMainLooper()).post {
                    binding.previewImageView.load(url = it.toString(), corner = 4f)
                }

                uriList.add(it)
                flashLight(false)
                false
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "파일이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                flashLight(false)
                false
            }
        }
    }

    private fun bindPreviewImageViewClickListener() = with(binding) {
        previewImageView.setOnClickListener {
            startActivityForResult(
                ImagePreviewListActivity.newIntent(this@CameraActivity, uriList),
                REQUEST_CODE_CONFIRM_IMAGE
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_CONFIRM_IMAGE && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }

    companion object {
        private const val REQUEST_CODE_CONFIRM_IMAGE = 3000
        private const val REQUEST_CODE_PERMISSIONS = 2000
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        private const val LENS_FACING: Int = CameraSelector.LENS_FACING_BACK

        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"

        fun newIntent(activity: Activity) = Intent(activity, CameraActivity::class.java)
    }
}