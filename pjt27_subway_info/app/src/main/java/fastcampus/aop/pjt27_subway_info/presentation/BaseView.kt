package fastcampus.aop.pjt27_subway_info.presentation

interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT
}