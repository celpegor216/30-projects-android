package fastcampus.aop.pjt30_food_delivery.util.mapper

import android.view.LayoutInflater
import android.view.ViewGroup
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderEmptyBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderFoodMenuBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderLikeRestaurantBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderOrderBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderOrderMenuBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderRestaurantBinding
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderRestaurantReviewBinding
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.Model
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.EmptyViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.ModelViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant.RestaurantViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.food.FoodMenuViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.order.OrderMenuViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.order.OrderViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant.LikeRestaurantViewHolder
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.review.RestaurantReviewViewHolder

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

            CellType.LIKE_RESTAURANT_CELL -> LikeRestaurantViewHolder(
                ViewholderLikeRestaurantBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.FOOD_CELL -> FoodMenuViewHolder(
                ViewholderFoodMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.ORDER_FOOD_CELL -> OrderMenuViewHolder(
                ViewholderOrderMenuBinding.inflate(inflater, parent, false),
                viewModel,
                resourcesProvider
            )

            CellType.ORDER_CELL -> OrderViewHolder(
                ViewholderOrderBinding.inflate(inflater, parent, false),
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