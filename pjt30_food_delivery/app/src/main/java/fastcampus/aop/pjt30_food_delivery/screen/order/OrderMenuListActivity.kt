package fastcampus.aop.pjt30_food_delivery.screen.order

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityOrderMenuListBinding
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseActivity
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.ModelRecyclerAdapter
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.order.OrderMenuListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderMenuListActivity : BaseActivity<OrderMenuListViewModel, ActivityOrderMenuListBinding>() {

    override val viewModel by viewModel<OrderMenuListViewModel>()

    override fun getViewBinding() = ActivityOrderMenuListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, OrderMenuListViewModel>(
            listOf(), viewModel, resourcesProvider, adapterListener = object :
                OrderMenuListListener {
                override fun onRemoveItem(model: FoodModel) {
                    viewModel.removeOrderMenu(model)
                }
            }
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        toolbar.setNavigationOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {
            viewModel.orderMenu()
        }

        orderClearButton.setOnClickListener {
            viewModel.clearOrderMenu()
        }
    }

    override fun observeData() = viewModel.orderMenuStateLiveData.observe(this) {
        when (it) {
            is OrderMenuState.Loading -> handleLoading()
            is OrderMenuState.Order -> handleOrder()
            is OrderMenuState.Success -> handleSuccess(it)
            is OrderMenuState.Error -> handleError(it)
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleOrder() {
        Toast.makeText(this, "주문을 완료했습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun handleSuccess(state: OrderMenuState.Success) = with(binding) {
        progressBar.isGone = true
        adapter.submitList(state.restaurantFoodModelList)

        val isOrderMenuListEmpty = state.restaurantFoodModelList.isNullOrEmpty()
        confirmButton.isEnabled = isOrderMenuListEmpty.not()

        if (isOrderMenuListEmpty) {
            Toast.makeText(this@OrderMenuListActivity, "주문 메뉴가 없어 화면을 종료합니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun handleError(state: OrderMenuState.Error) {
        Toast.makeText(this, getString(state.messageId, state.e.message), Toast.LENGTH_SHORT).show()
        finish()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, OrderMenuListActivity::class.java)
    }
}