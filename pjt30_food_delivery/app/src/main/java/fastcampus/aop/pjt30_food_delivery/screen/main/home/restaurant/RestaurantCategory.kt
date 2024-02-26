package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant

import androidx.annotation.StringRes
import fastcampus.aop.pjt30_food_delivery.R

enum class RestaurantCategory(
    @StringRes val categroyNameId: Int,
    @StringRes val categoryTypeId: Int
) {
    ALL(R.string.all, R.string.all_type)
}