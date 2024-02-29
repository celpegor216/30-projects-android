package fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail

import android.app.AlertDialog
import android.content.ClipDescription
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityRestaurantDetailBinding
import fastcampus.aop.pjt30_food_delivery.extension.fromDpToPx
import fastcampus.aop.pjt30_food_delivery.extension.load
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseActivity
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.menu.RestaurantMenuListFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.detail.review.RestaurantReviewListFragment
import fastcampus.aop.pjt30_food_delivery.widget.adapter.RestaurantDetailListFragmentPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.math.abs


class RestaurantDetailActivity :
    BaseActivity<RestaurantDetailViewModel, ActivityRestaurantDetailBinding>() {

    override val viewModel by viewModel<RestaurantDetailViewModel> {
        parametersOf(
            intent.getParcelableExtra<RestaurantEntity>(RestaurantListFragment.RESTAURANT_KEY)
        )
    }

    override fun getViewBinding() = ActivityRestaurantDetailBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantDetailListFragmentPagerAdapter

    override fun initViews() {
        super.initViews()

        initAppBar()
    }

    private fun initAppBar() = with(binding) {
        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val topPadding = 300f.fromDpToPx().toFloat()
            val realAlphaScrollHeight = appBarLayout.measuredHeight - appBarLayout.totalScrollRange
            val abstractOffset = abs(verticalOffset)

            // 300 이상 스크롤한 경우
            if (abstractOffset < topPadding) {
                restaurantTitleTextView.alpha = 0f
                return@OnOffsetChangedListener
            }

            val realAlphaVerticalOffset = abstractOffset - topPadding
            val percentage = realAlphaVerticalOffset / realAlphaScrollHeight
            restaurantTitleTextView.alpha =
                1 - (if (1 - percentage * 2 < 0) 0f else 1 - percentage * 2)
        })

        toolbar.setNavigationOnClickListener { finish() }

        callButton.setOnClickListener {
            viewModel.getRestaurantTelNumber()?.let { telNumber ->
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$telNumber"))
                startActivity(intent)
            }
        }

        likeButton.setOnClickListener {
            viewModel.toggleLikedRestaurant()
        }

        shareButton.setOnClickListener {
            viewModel.getRestaurantInfo()?.let { restaurantInfo ->
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = ClipDescription.MIMETYPE_TEXT_PLAIN
                    putExtra(
                        Intent.EXTRA_TEXT, "맛있는 음식점: ${restaurantInfo.restaurantTitle}" +
                                "\n평점: ${restaurantInfo.grade}" +
                                "\n연락처: ${restaurantInfo.restaurantTelNumber}"
                    )
                    Intent.createChooser(this, "친구에게 공유하기")
                }
                startActivity(intent)
            }
        }
    }

    override fun observeData() = viewModel.restaurantDetailStateLiveData.observe(this) {
        when (it) {
            is RestaurantDetailState.Loading -> handleLoading()
            is RestaurantDetailState.Success -> handleSuccess(it)
            else -> Unit
        }
    }

    private fun handleLoading() = with(binding) {
        progressBar.isVisible = true
    }

    private fun handleSuccess(state: RestaurantDetailState.Success) = with(binding) {
        progressBar.isGone = true

        val restaurantEntity = state.restaurantEntity

        callButton.isGone = restaurantEntity.restaurantTelNumber == null
        restaurantTitleTextView.text = restaurantEntity.restaurantTitle
        restaurantImageView.load(restaurantEntity.restaurantImageUrl)
        restaurantMainTitleTextView.text = restaurantEntity.restaurantTitle
        ratingBar.rating = restaurantEntity.grade
        deliveryTimeTextView.text = getString(
            R.string.delivery_time_expected_format,
            restaurantEntity.deliveryTimeRange.first,
            restaurantEntity.deliveryTimeRange.second
        )
        deliveryTipTextView.text = getString(
            R.string.delivery_tip_expected_format,
            restaurantEntity.deliveryTipRange.first,
            restaurantEntity.deliveryTipRange.second
        )
        likeTextView.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this@RestaurantDetailActivity, if (state.isLiked == true) {
                    R.drawable.ic_heart_enable
                } else {
                    R.drawable.ic_heart_disable
                }
            ),
            null, null, null
        )

        if (::viewPagerAdapter.isInitialized.not()) {
            initViewPager(
                state.restaurantEntity.restaurantInfoId,
                state.restaurantFoodList,
                state.restaurantEntity.restaurantTitle
            )
        }

        notifyBasketCount(state.foodMenuListInBasket)

        val (isClearNeed, afterAction) = state.isClearNeedInBasketAndAction
        if (isClearNeed) {
            alertClearNeedInBasket(afterAction)
        }
    }

    private fun alertClearNeedInBasket(afterAction: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("장바구니에는 한 가게의 메뉴만 담을 수 있습니다.")
            .setMessage("선택하신 메뉴를 장바구니에 담을 경우,\n다른 가게의 메뉴가 삭제됩니다.")
            .setPositiveButton("담기") { dialog, _ ->
                viewModel.notifyClearBasket()
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun notifyBasketCount(foodMenuListInBasket: List<RestaurantFoodEntity>?) =
        with(binding) {
            basketCountTextView.text = if (foodMenuListInBasket.isNullOrEmpty()) {
                "0"
            } else {
                getString(R.string.basket_count_format, foodMenuListInBasket.size)
            }

            basketButton.setOnClickListener {
                // TODO 주문 화면으로 이동
            }
        }

    private fun initViewPager(
        restaurantInfoId: Long,
        restaurantFoodList: List<RestaurantFoodEntity>?,
        restaurantTitle: String
    ) {
        viewPagerAdapter = RestaurantDetailListFragmentPagerAdapter(
            this,
            listOf(
                RestaurantMenuListFragment.newInstance(
                    restaurantInfoId,
                    ArrayList(restaurantFoodList ?: listOf())
                ),
                RestaurantReviewListFragment.newInstance(
                    restaurantTitle
                )
            )
        )

        binding.menuAndReviewViewPager.adapter = viewPagerAdapter
        TabLayoutMediator(
            binding.menuAndReviewTabLayout,
            binding.menuAndReviewViewPager
        ) { tab, position ->
            tab.setText(RestaurantDetailCategory.entries[position].categoryNameId)
        }.attach()
    }

    companion object {
        fun newIntent(context: Context, restaurantEntity: RestaurantEntity) =
            Intent(context, RestaurantDetailActivity::class.java).apply {
                putExtra(RestaurantListFragment.RESTAURANT_KEY, restaurantEntity)
            }
    }
}