package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.menu

import android.widget.Toast
import androidx.core.os.bundleOf
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.databinding.FragmentListBinding
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.RestaurantDetailViewModel
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.ModelRecyclerAdapter
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant.FoodMenuListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantMenuListFragment :
    BaseFragment<RestaurantMenuListViewModel, FragmentListBinding>() {

    private val restaurantId by lazy {
        arguments?.getLong(RESTAURANT_ID_KEY, -1)
    }

    private val restaurantFoodList by lazy {
        arguments?.getParcelableArrayList<RestaurantFoodEntity>(FOOD_LIST_KEY)
    }

    override val viewModel by viewModel<RestaurantMenuListViewModel> {
        parametersOf(restaurantId, restaurantFoodList)
    }

    private val restaurantDetailViewModel by sharedViewModel<RestaurantDetailViewModel>()

    override fun getViewBinding() = FragmentListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<FoodModel, RestaurantMenuListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : FoodMenuListListener {
                override fun onClickItem(model: FoodModel) {
                    viewModel.insertMenuIntoBasket(model)
                }
            }
        )
    }

    override fun initViews() {
        binding.recyclerView.adapter = adapter
    }

    override fun observeData() {
        viewModel.restaurantMenuListLiveData.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        viewModel.menuBasketLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "장바구니에 ${it.title} 메뉴가 담겼습니다.", Toast.LENGTH_SHORT).show()
            restaurantDetailViewModel.notifyFoodMenuListInBasketChanged(it)
        }

        viewModel.isClearNeedInBasketLiveData.observe(viewLifecycleOwner) { (isClearNeed, afterAction) ->
            if (isClearNeed) {
                restaurantDetailViewModel.notifyClearNeedAlertInBasket(isClearNeed, afterAction)
            }
        }
    }

    companion object {
        private const val RESTAURANT_ID_KEY = "restaurantId"
        private const val FOOD_LIST_KEY = "foodList"
        fun newInstance(
            restaurantId: Long,
            foodList: ArrayList<RestaurantFoodEntity>
        ): RestaurantMenuListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId,
                FOOD_LIST_KEY to foodList
            )

            return RestaurantMenuListFragment().apply {
                arguments = bundle
            }
        }
    }
}