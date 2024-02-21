package fastcampus.aop.pjt28_delivery_info.presentation

interface BaseView<PresenterT: BasePresenter> {

    val presenter: PresenterT
}