package fastcampus.aop.pjt29_movie_review.presentation

interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT
}