package fastcampus.aop.pjt29_movie_review.presentation.reviews

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt29_movie_review.databinding.FragmentReviewsBinding
import fastcampus.aop.pjt29_movie_review.domain.model.Movie
import fastcampus.aop.pjt29_movie_review.domain.model.MovieReviews
import fastcampus.aop.pjt29_movie_review.domain.model.Review
import fastcampus.aop.pjt29_movie_review.extension.toGone
import fastcampus.aop.pjt29_movie_review.extension.toVisible
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment
import org.koin.core.parameter.parametersOf

class ReviewsFragment : ScopeFragment(), ReviewsContract.View {

    override val presenter: ReviewsContract.Presenter by inject { parametersOf(arguments.movie) }

    private var binding: FragmentReviewsBinding? = null
    private val arguments: ReviewsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentReviewsBinding.inflate(inflater, container, false)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
    }

    override fun showErrorDescription(message: String) {
        binding?.recyclerView?.toGone()
        binding?.errorDescriptionTextView?.toVisible()
        binding?.errorDescriptionTextView?.text = message
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun showMovieInformation(movie: Movie) {
        binding?.recyclerView?.adapter = ReviewsAdapter(movie).apply {
            onReviewSubmitButtonClickListener = { content, score ->
                presenter.requestAddReview(content, score)
                hideKeyboard()
            }
            onReviewDeleteButtonClickListener = { review ->
                showDeleteConfirmDialog(review)
            }
        }
    }

    override fun showReviews(movieReviews: MovieReviews) {
        binding?.recyclerView?.toVisible()
        binding?.errorDescriptionTextView?.toGone()
        (binding?.recyclerView?.adapter as? ReviewsAdapter)?.apply {
            this.myReview = movieReviews.myReview
            this.reviews = movieReviews.othersReview
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        }
    }

    private fun showDeleteConfirmDialog(review: Review) {
        AlertDialog.Builder(requireContext())
            .setMessage("정말로 리뷰를 삭제하시겠습니까?")
            .setPositiveButton("확인") { _, _ ->
                presenter.requestDeleteReview(review)
            }
            .setNegativeButton("취소") { _, _ ->}
            .show()
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}