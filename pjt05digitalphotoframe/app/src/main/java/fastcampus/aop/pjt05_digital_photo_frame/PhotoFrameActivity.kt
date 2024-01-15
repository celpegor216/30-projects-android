package fastcampus.aop.pjt05_digital_photo_frame

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import java.util.Timer
import kotlin.concurrent.timer

class PhotoFrameActivity: AppCompatActivity() {
    private val photoImageView by lazy {
        findViewById<ImageView>(R.id.photoImageView)
    }

    private val backgroundPhotoImageView by lazy {
        findViewById<ImageView>(R.id.backgroundPhotoImageView)
    }

    private var currentPosition = 0

    private var timer: Timer? = null

    private val photoList = mutableListOf<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_frame)

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val size = intent.getIntExtra("photoListSize", 0)
        for (i in 0..size) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = kotlin.concurrent.timer(period = 5 * 1000) {
            runOnUiThread {
                val current = currentPosition
                val next = if (photoList.size <= currentPosition + 1) 0 else currentPosition + 1

                backgroundPhotoImageView.setImageURI(photoList[current])

                photoImageView.alpha = 0f
                photoImageView.setImageURI(photoList[next])
                photoImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()

        // 백그라운드에서 자원을 낭비하는 것을 막기 위함
        timer?.cancel()
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }
}