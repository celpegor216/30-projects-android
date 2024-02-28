package fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.restaurant

import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderRestaurantBinding
import fastcampus.aop.pjt30_food_delivery.extension.clear
import fastcampus.aop.pjt30_food_delivery.extension.load
import fastcampus.aop.pjt30_food_delivery.model.restaurant.RestaurantModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant.RestaurantListListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.ModelViewHolder

class RestaurantViewHolder(
    private val binding: ViewholderRestaurantBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
): ModelViewHolder<RestaurantModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        restaurantImageView.clear()
    }

    override fun bindViews(model: RestaurantModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is RestaurantListListener) {
            root.setOnClickListener {
                adapterListener.onClickItem(model)
            }
        }
    }

    override fun bindData(model: RestaurantModel) = with(binding) {
        super.bindData(model)

        restaurantImageView.load(model.restaurantImageUrl, 24f)
        restaurantTitleTextView.text = model.restaurantTitle
        gradeTextView.text = resourcesProvider.getString(R.string.grade_format, model.grade)
        reviewCountTextView.text = resourcesProvider.getString(R.string.review_count_format, model.reviewCount)

        val (minTime, maxTime) = model.deliveryTimeRange
        deliveryTimeTextView.text = resourcesProvider.getString(R.string.delivery_time_format, minTime, maxTime)

        val (minTip, maxTip) = model.deliveryTipRange
        deliveryTipTextView.text = resourcesProvider.getString(R.string.delivery_tip_format, minTip, maxTip)
    }
}