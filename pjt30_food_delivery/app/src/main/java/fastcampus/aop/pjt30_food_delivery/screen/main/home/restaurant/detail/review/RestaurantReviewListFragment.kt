package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review

import androidx.core.os.bundleOf
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.databinding.FragmentListBinding
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RestaurantReviewListFragment: BaseFragment<RestaurantReviewListViewModel, FragmentListBinding>() {

    override val viewModel by viewModel<RestaurantReviewListViewModel>()

    override fun getViewBinding() = FragmentListBinding.inflate(layoutInflater)

    override fun observeData() {
    }

    companion object {
        private const val RESTAURANT_ID_KEY = "restaurantId"
        fun newInstance(
            restaurantId: Long,
        ): RestaurantReviewListFragment {
            val bundle = bundleOf(
                RESTAURANT_ID_KEY to restaurantId
            )

            return RestaurantReviewListFragment().apply {
                arguments = bundle
            }
        }
    }
}