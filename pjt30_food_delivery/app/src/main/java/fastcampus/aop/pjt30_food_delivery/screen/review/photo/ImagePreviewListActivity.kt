package fastcampus.aop.pjt30_food_delivery.screen.review.photo

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityImageListBinding
import fastcampus.aop.pjt30_food_delivery.util.path.PathUtil
import fastcampus.aop.pjt30_food_delivery.widget.adapter.ImageViewPagerAdapter
import java.io.File
import java.io.FileNotFoundException

class ImagePreviewListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImageListBinding
    private lateinit var imageViewPagerAdapter: ImageViewPagerAdapter

    private val uriList by lazy<List<Uri>> { intent.getParcelableArrayListExtra(KEY_URI_LIST)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        setupImageList()
    }

    private fun setupImageList() = with(binding) {
        if (::imageViewPagerAdapter.isInitialized.not()) {
            imageViewPagerAdapter = ImageViewPagerAdapter(uriList)
        }
        imageViewPager.adapter = imageViewPagerAdapter
        indicator.setViewPager(imageViewPager)
        imageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                toolbar.title = getString(R.string.images_page, position + 1, imageViewPagerAdapter.itemCount)
            }
        })

        deleteButton.setOnClickListener {
            removeImage(uriList[imageViewPager.currentItem])
        }

        confirmButton.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(KEY_URI_LIST, ArrayList<Uri>().apply { imageViewPagerAdapter.uriList.forEach { add(it) } })
            })
            finish()
        }
    }

    private fun removeImage(uri: Uri) {
        try {
            val file = File(PathUtil.getPath(this, uri) ?: throw FileNotFoundException())
            file.delete()
            imageViewPagerAdapter.uriList.let {
                val imageList = it.toMutableList()
                imageList.remove(uri)
                imageViewPagerAdapter.uriList = imageList
                imageViewPagerAdapter.notifyDataSetChanged()
            }

            MediaScannerConnection.scanFile(this, arrayOf(file.path), arrayOf("image/jpeg"), null)

            binding.indicator.setViewPager(binding.imageViewPager)

            if (imageViewPagerAdapter.uriList.isEmpty()) {
                Toast.makeText(this, "삭제할 수 있는 사진이 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this, "사진이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val KEY_URI_LIST = "uriList"

        fun newIntent(activity: Activity, urlList: List<Uri>) =
            Intent(activity, ImagePreviewListActivity::class.java).apply {
                putExtra(KEY_URI_LIST, ArrayList<Uri>().apply { urlList.forEach { add(it) } })
            }
    }
}