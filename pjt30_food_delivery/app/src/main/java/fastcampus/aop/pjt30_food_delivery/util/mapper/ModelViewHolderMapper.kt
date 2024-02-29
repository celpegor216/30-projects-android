package fastcampus.aop.pjt30_food_delivery.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderEmptyBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderFoodMenuBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderRestaurantBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderRestaurantReviewBinding
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.Model
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.EmptyViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.ModelViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant.food.FoodMenuViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant.review.RestaurantReviewViewHolder

object ModelViewHolderMapper {

    // return 값의 type casting 방지
    @Suppress("UNCHECKED_CAST")
    fun <M : Model> map(
        parent: ViewGroup,
        type: CellType,
        viewModel: BaseViewModel,
        resourcesProvider: ResourcesProvider
    ): ModelViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder = when (type) {
            CellType.EMPTY_CELL -> EmptyViewHolder(
                ViewholderEmptyBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.RESTAURANT_CELL -> RestaurantViewHolder(
                ViewholderRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.REVIEW_CELL -> RestaurantReviewViewHolder(
                ViewholderRestaurantReviewBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )
        }
        return viewHolder as ModelViewHolder<M>
    }
}