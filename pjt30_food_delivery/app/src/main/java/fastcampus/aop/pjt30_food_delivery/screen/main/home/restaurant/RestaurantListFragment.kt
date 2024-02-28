package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant

import androidx.core.os.bundleOf
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.databinding.FragmentListBinding
import fastcampus.aop.pjt30_food_delivery.model.restaurant.RestaurantModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.RestaurantDetailActivity
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.ModelRecyclerAdapter
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.restaurant.RestaurantListListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantListFragment :
    BaseFragment<RestaurantListViewModel, FragmentListBinding>() {

    private val restaurantCategory by lazy {
        arguments?.getSerializable(RESTAURANT_CATEGORY_KEY) as RestaurantCategory
    }
    private val locationLatLng by lazy {
        arguments?.getParcelable<LocationLatLngEntity>(LOCATION_KEY)
    }

    override val viewModel by viewModel<RestaurantListViewModel> {
        parametersOf(restaurantCategory, locationLatLng)
    }

    override fun getViewBinding(): FragmentListBinding =
        FragmentListBinding.inflate(layoutInflater)

    private val resourcesProvider by inject<ResourcesProvider>()

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantModel, RestaurantListViewModel>(listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : RestaurantListListener {
                override fun onClickItem(model: RestaurantModel) {
                    startActivity(
                        RestaurantDetailActivity.newIntent(
                            requireContext(), model.toEntity()
                        )
                    )
                }
            })
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.restaurantListLiveData.observe(viewLifecycleOwner) {
        adapter.submitList(it)
    }

    companion object {
        const val RESTAURANT_CATEGORY_KEY = "restaurantCategory"
        const val LOCATION_KEY = "location"
        const val RESTAURANT_KEY = "restaurant"

        fun newInstance(
            restaurantCategory: RestaurantCategory, locationLatLngEntity: LocationLatLngEntity
        ): RestaurantListFragment {
            return RestaurantListFragment().apply {
                arguments = bundleOf(
                    RESTAURANT_CATEGORY_KEY to restaurantCategory,
                    LOCATION_KEY to locationLatLngEntity
                )
            }
        }
    }
}