package fastcampus.aop.pjt30_food_delivery.screen.main.home

sealed class HomeState {

    object Uninitialized: HomeState()

    object Loading: HomeState()

    object Success: HomeState()

}