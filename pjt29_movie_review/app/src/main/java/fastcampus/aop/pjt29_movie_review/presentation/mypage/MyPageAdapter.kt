package fastcampus.aop.pjt29_movie_review.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fastcampus.aop.pjt29_movie_review.databinding.ItemReviewedMovieBinding
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.ReviewedMovie
import fastcampus.aop.pjt29_movie_review.extension.toDecimalFormatString

class MyPageAdapter : RecyclerView.Adapter<MyPageAdapter.ViewHolder>() {

    var reviewedMovies: List<ReviewedMovie> = emptyList()
    var onMovieClickListener: ((Movie) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemReviewedMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onMovieClickListener?.invoke(reviewedMovies[adapterPosition].movie)
            }
        }

        fun bind(item: ReviewedMovie) {
            Glide.with(binding.root)
                .load(item.movie.posterUrl)
                .into(binding.posterImageView)

            binding.myScoreTextView.text = item.myReview.score?.toDecimalFormatString("0.0")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageAdapter.ViewHolder =
        ViewHolder(
            ItemReviewedMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    override fun onBindViewHolder(holder: MyPageAdapter.ViewHolder, position: Int) {
        holder.bind(reviewedMovies[position])
    }

    override fun getItemCount(): Int = reviewedMovies.size
}