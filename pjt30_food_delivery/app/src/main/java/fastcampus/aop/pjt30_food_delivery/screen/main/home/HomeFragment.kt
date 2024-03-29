package fastcampus.aop.pjt30_food_delivery.screen.main.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity
import fastcampus.aop.pjt30_food_delivery.databinding.FragmentHomeBinding
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.MainActivity
import fastcampus.aop.pjt30_food_delivery.screen.main.MainTabMenu
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListFragment
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantOrder
import fastcampus.aop.pjt30_food_delivery.screen.mylocation.MyLocationActivity
import fastcampus.aop.pjt30_food_delivery.screen.order.OrderMenuListActivity
import fastcampus.aop.pjt30_food_delivery.widget.adapter.RestaurantListFragmentPagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    override val viewModel by viewModel<HomeViewModel>()

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var viewPagerAdapter: RestaurantListFragmentPagerAdapter

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }

    private lateinit var locationManager: LocationManager
    private lateinit var myLocationListener: MyLocationListener

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermissions = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_FINE_LOCATION) || (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
            }

            if (responsePermissions.filter { it.value }.size == locationPermissions.size) {
                setMyLocationListener()
            } else {
                with(binding.locationTitleText) {
                    text = getString(R.string.please_setup_your_location_permission)
                    setOnClickListener {
                        getMyLocation()
                    }
                }

                Toast.makeText(
                    requireContext(),
                    getString(R.string.can_not_assigned_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val changeLocationLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
                    ?.let { myLocationInfo ->
                        viewModel.loadReverseGeoInformation(myLocationInfo.locationLatLng)
                    }
            }
        }

    override fun initViews() = with(binding) {
        locationTitleText.setOnClickListener {
            changeLocation()
        }

        orderChipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.chipDefault -> {
                    chipInitialize.isGone = true
                    changeRestaurantOrder(RestaurantOrder.DEFAULT)
                }
                R.id.chipInitialize -> {
                    chipDefault.isChecked = true
                }
                R.id.chipLowDeliveryTip -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.LOW_DELIVERY_TIP)
                }
                R.id.chipFastDelivery -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.FAST_DELIVERY)
                }
                R.id.chipTopRate -> {
                    chipInitialize.isVisible = true
                    changeRestaurantOrder(RestaurantOrder.TOP_RATE)
                }
            }
        }
    }

    private fun changeRestaurantOrder(order: RestaurantOrder) {
        viewPagerAdapter.fragmentList.forEach {
            it.viewModel.setRestaurantOrder(order)
        }
    }

    private fun changeLocation() {
        viewModel.getMapSearchInfo()?.let { mapSearchInfo ->

            changeLocationLauncher.launch(
                MyLocationActivity.newIntent(requireContext(), mapSearchInfo)
            )
        }
    }

    private fun initViewPager(locationLatLngEntity: LocationLatLngEntity) = with(binding) {
        val restaurantCategories = RestaurantCategory.values()

        if (::viewPagerAdapter.isInitialized.not()) {
            orderChipGroup.isVisible = true

            val restaurantListFragmentPages = restaurantCategories.map {
                RestaurantListFragment.newInstance(it, locationLatLngEntity)
            }

            viewPagerAdapter = RestaurantListFragmentPagerAdapter(
                this@HomeFragment,
                restaurantListFragmentPages,
                locationLatLngEntity
            )
            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit = restaurantCategories.size
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.setText(restaurantCategories[position].categoryNameId)
            }.attach()
        }

        if (locationLatLngEntity != viewPagerAdapter.locationLatLngEntity) {
            viewPagerAdapter.locationLatLngEntity = locationLatLngEntity
            viewPagerAdapter.fragmentList.forEach {
                it.viewModel.setLocationLatLng(locationLatLngEntity)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.checkMyBasket()
    }

    override fun observeData() {
        viewModel.homeStateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is HomeState.Uninitialized -> getMyLocation()
                HomeState.Loading -> {
                    binding.locationLoading.isVisible = true
                    binding.locationTitleText.text = getString(R.string.loading)
                }

                is HomeState.Success -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleText.text = it.mapSearchInfo.fullAddress
                    binding.locationTitleText.setOnClickListener {
                        changeLocation()
                    }
                    binding.tabLayout.isVisible = true
                    binding.filterScrollView.isVisible = true
                    binding.viewPager.isVisible = true
                    initViewPager(it.mapSearchInfo.locationLatLng)

                    if (it.isLocationSame.not()) {
                        Toast.makeText(requireContext(),
                            getString(R.string.please_check_location), Toast.LENGTH_SHORT).show()
                    }
                }

                is HomeState.Error -> {
                    binding.locationLoading.isGone = true
                    binding.locationTitleText.text = getString(R.string.can_not_load_address_info)
                    binding.locationTitleText.setOnClickListener {
                        getMyLocation()
                    }
                    Toast.makeText(requireContext(), getString(it.messageId), Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.foodMenuBasketLiveData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.basketButtonContainer.isVisible = true
                binding.basketCountTextView.text = getString(R.string.basket_count_format, it.size)
                binding.basketButton.setOnClickListener {
                    if (firebaseAuth.currentUser == null) {
                        alertLoginNeed {
                            (requireActivity() as MainActivity).goToTab(MainTabMenu.MY)
                        }
                    } else {
                        startActivity(OrderMenuListActivity.newIntent(requireContext()))
                    }
                }
            } else {
                binding.basketButtonContainer.isGone = true
                binding.basketButton.setOnClickListener(null)
            }
        }
    }

    private fun alertLoginNeed(afterAction: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle("로그인이 필요합니다.")
            .setMessage("My 탭으로 이동하시겠습니까?")
            .setPositiveButton("네") { dialog, _ ->
                afterAction()
                dialog.dismiss()
            }
            .setNegativeButton("아니오") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {
            locationPermissionLauncher.launch(locationPermissions)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setMyLocationListener() {
        val minTime = 1500L
        val minDistance = 100f
        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, minTime, minDistance, myLocationListener
            )
        }
    }

    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }

    companion object {
        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"

        private val locationPermissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            viewModel.loadReverseGeoInformation(
                LocationLatLngEntity(
                    latitude = location.latitude, longitude = location.longitude
                )
            )
            removeLocationListener()
        }
    }
}