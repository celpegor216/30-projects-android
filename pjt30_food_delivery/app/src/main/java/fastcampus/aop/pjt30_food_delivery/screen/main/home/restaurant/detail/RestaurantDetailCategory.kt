package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail

import androidx.annotation.StringRes
import fastcampus.aop.pjt30_food_delivery.R

enum class RestaurantDetailCategory(
    @StringRes val categoryNameId: Int
) {
    MENU(R.string.menu),
    REVIEW(R.string.review)
}