package fastcampus.aop.pjt29_movie_review.presentation.mypage

import fastcampus.aop.pjt29_movie_review.domain.usecase.GetMyReviewedMoviesUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MyPagePresenter(
    private val view: MyPageContract.View,
    private val getMyReviewedMovies: GetMyReviewedMoviesUseCase
): MyPageContract.Presenter {

    override val scope: CoroutineScope = MainScope()

    override fun onViewCreated() {
        fetchReviewedMovies()
    }

    override fun onDestroyView() {}

    private fun fetchReviewedMovies() = scope.launch {
        try {
            view.showLoadingIndicator()

            val reviewedMovies = getMyReviewedMovies()
            if (reviewedMovies.isNullOrEmpty()) {
                view.showNoDataDescription("작성한 리뷰가 없습니다.\n홈 탭을 눌러 영화를 리뷰해보세요.")
            } else {
                view.showReviewedMovies(reviewedMovies)
            }

        } catch (e: Exception) {
            e.printStackTrace()
            view.showErrorDescription("오류가 발생했습니다.")
        } finally {
            view.hideLoadingIndicator()
        }
    }
}