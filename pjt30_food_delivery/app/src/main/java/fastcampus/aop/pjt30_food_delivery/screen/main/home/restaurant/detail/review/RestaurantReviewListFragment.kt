package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import fastcampus.aop.pjt30_food_delivery.databinding.FragmentListBinding
import fastcampus.aop.pjt30_food_delivery.model.restaurant.review.RestaurantReviewModel
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import fastcampus.aop.pjt30_food_delivery.util.provider.ResourcesProvider
import fastcampus.aop.pjt30_food_delivery.widget.adapter.ModelRecyclerAdapter
import fastcampus.aop.pjt30_food_delivery.widget.adapter.listener.AdapterListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RestaurantReviewListFragment :
    BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel by viewModel<RestaurantReviewListViewModel> {
        parametersOf(
            arguments?.getString(RESTAURANT_TITLE_KEY)
        )
    }

    private val resourcesProvider by inject<ResourcesProvider>()

    override fun getViewBinding() = FragmentListBinding.inflate(layoutInflater)

    private val adapter by lazy {
        ModelRecyclerAdapter<RestaurantReviewModel, RestaurantReviewListViewModel>(
            listOf(),
            viewModel,
            resourcesProvider,
            adapterListener = object : AdapterListener {}
        )
    }

    override fun initViews() = with(binding) {
        recyclerView.adapter = adapter
    }

    override fun observeData() = viewModel.reviewStateLiveData.observe(viewLifecycleOwner) {
        when (it) {
            is RestaurantReviewState.Loading -> {}
            is RestaurantReviewState.Success -> handleSuccess(it)
            else -> {}
        }
    }

    private fun handleSuccess(state: RestaurantReviewState.Success) {
        adapter.submitList(state.reviewList)
    }

    companion object {
        private const val RESTAURANT_TITLE_KEY = "restaurantId"
        fun newInstance(restaurantTitle: String): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_TITLE_KEY to restaurantTitle
            )

            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}