package fastcampus.aop.pjt12_book_review

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import fastcampus.aop.pjt12_book_review.databinding.ActivityDetailBinding
import fastcampus.aop.pjt12_book_review.model.Book
import fastcampus.aop.pjt12_book_review.model.Review

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getAppDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread {
            val review = db.reviewDao().getReview(model?.id.orEmpty())

            if (review != null) {
                runOnUiThread {
                    binding.reviewEditText.setText(review.review.orEmpty())
                }
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        model?.id.orEmpty(),
                        binding.reviewEditText.text.toString()))
            }.start()
        }
    }
}