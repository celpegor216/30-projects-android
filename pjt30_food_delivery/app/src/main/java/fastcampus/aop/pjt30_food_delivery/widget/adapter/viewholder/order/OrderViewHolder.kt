package fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.order

import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderOrderBinding
import fastcampus.aop.pjt30_food_delivery.model.order.OrderModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.order.OrderListListener
import fastcampus.aop.pjt30_food_delivery.widget.adapter.viewholder.ModelViewHolder

class OrderViewHolder(
    private val binding: ViewholderOrderBinding,
    viewModel: BaseViewModel,
    resourcesProvider: ResourcesProvider
) : ModelViewHolder<OrderModel>(binding, viewModel, resourcesProvider) {

    override fun reset() = Unit

    override fun bindData(model: OrderModel) = with(binding) {
        super.bindData(model)

        orderTitleTextView.text =
            resourcesProvider.getString(R.string.order_history_title_format, model.orderId)

        val foodMenuList = model.foodMenuList

        foodMenuList.groupBy { it.title }.entries.forEach { (title, menuList) ->
            val orderDataStr =
                orderContentTextView.text.toString() + "메뉴 : $title | 가격 : ${menuList.first().price}원 X ${menuList.size}\n"
            orderContentTextView.text = orderDataStr
        }
        orderContentTextView.text = orderContentTextView.text.trim()

        orderTotalPriceTextView.text = resourcesProvider.getString(
            R.string.price_format,
            foodMenuList.map { it.price }.reduce { total, price -> total + price })
    }

    override fun bindViews(model: OrderModel, adapterListener: AdapterListener) = with(binding) {
        if (adapterListener is OrderListListener) {
            root.setOnClickListener {
                adapterListener.writeReview(model.orderId, model.restaurantTitle)
            }
        }
    }
}