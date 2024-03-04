package fastcampus.aop.pjt30_food_delivery.screen.main.like

import androidx.core.view.isGone
import androidx.core.view.isVisible
import fastcampus.aop.pjt30_food_delivery.databinding.FragmentRestaurantLikeListBinding
import fastcampus.aop.pjt30_food_delivery.model.restaurant.RestaurantModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.RestaurantDetailActivity
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.ModelRecyclerAdapter
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant.RestaurantLikeListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantLikeListFragment: BaseFragment<RestaurantLikeListViewModel, FragmentRestaurantLikeListBinding>() {

    override val viewModel by viewModel<RestaurantLikeListViewModel>()

    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding() = FragmentRestaurantLikeListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantLikeListViewModel>(listOf(), viewModel, resourcesProvider, adapterListener = object :
            RestaurantLikeListListener {
            override fun onClickItem(model: RestaurantModel) {
                startActivity(RestaurantDetailActivity.newIntent(requireContext(), model.toEntity()))
            }

            override fun onDislikeItem(model: RestaurantModel) {
                viewModel.dislikeRestaurant(model.toEntity())
            }
        })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantLikeListLiveData.observe(viewLifecycleOwner) {
        checkListEmpty(it)
    }

    private fun checkListEmpty(restaurantList: List<RestaurantModel>) = with(binding) {
        val isEmpty = restaurantList.isEmpty()
        recyclerView.isGone = isEmpty
        emptyResultTextView.isVisible = isEmpty
        if (isEmpty.not()) {
            adapter.submitList(restaurantList)
        }
    }

    companion object {
        const val TAG = "restaurantLikeListFragment"
        fun newInstance() = RestaurantLikeListFragment()
    }
}