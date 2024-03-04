package fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.order

import com.bumptech.glide.load.resource.bitmap.CenterCrop
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderOrderMenuBinding
import fastcampus.aop.pjt30_food_delivery.extension.clear
import fastcampus.aop.pjt30_food_delivery.extension.load
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.order.OrderMenuListListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant.FoodMenuListListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.ModelViewHolder

class OrderMenuViewHolder(
    private val binding: ViewholderOrderMenuBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<FoodModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = with(binding) {
        foodImageView.clear()
    }

    override fun bindData(model: FoodModel) = with(binding) {
        super.bindData(model)

        foodImageView.load(model.imageUrl, 24f, CenterCrop())
        foodTitleTextView.text = model.title
        foodDescriptionTextView.text = model.description
        foodPriceTextView.text = resourcesProvider.getString(R.string.price_format, model.price)
    }

    override fun bindViews(model: FoodModel, adapterListener: AdapterListener) {
        if (adapterListener is OrderMenuListListener) {
            binding.root.setOnClickListener {
                adapterListener.onRemoveItem(model)
            }
        }
    }
}