package fastcampus.aop.pjt30_food_delivery.screen.mylocation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.MapSearchInfoEntity
import fastcampus.aop.pjt30_food_delivery.databinding.ActivityMyLocationBinding
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseActivity
import fastcampus.aop.pjt30_food_delivery.screen.main.home.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyLocationActivity : BaseActivity<MyLocationViewModel, ActivityMyLocationBinding>(),
    OnMapReadyCallback {

    override val viewModel by viewModel<MyLocationViewModel> {
        parametersOf(
            intent.getParcelableExtra<MapSearchInfoEntity>(HomeViewModel.MY_LOCATION_KEY)
        )
    }

    private lateinit var map: GoogleMap

    private var isLocationChaned = false

    override fun getViewBinding(): ActivityMyLocationBinding =
        ActivityMyLocationBinding.inflate(layoutInflater)

    override fun initViews() = with(binding) {
        setupGoogleMap()

        toolbar.setNavigationOnClickListener {
            finish()
        }

        confirmButton.setOnClickListener {
            viewModel.confirmSelectLocation()
        }
    }

    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map ?: return
        viewModel.fetchData()
    }

    override fun observeData() = viewModel.myLocationStateLiveData.observe(this) {
        when (it) {
            is MyLocationState.Loading -> handleLoadingState()
            is MyLocationState.Success -> {
                if (::map.isInitialized) {
                    handleSuccessState(it)
                }
            }

            is MyLocationState.Confirm -> {
                setResult(Activity.RESULT_OK, Intent().apply {
                    putExtra(HomeViewModel.MY_LOCATION_KEY, it.mapSearchInfo)
                })
                finish()
            }
            is MyLocationState.Error -> {
                Toast.makeText(this, getString(it.messageId), Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    private fun handleLoadingState() = with(binding) {
        locationLoading.isVisible = true
        locationTitleText.text = getString(R.string.loading)
    }

    private fun handleSuccessState(state: MyLocationState.Success) = with(binding) {
        locationLoading.isGone = true

        val mapSearchInfo = state.mapSearchInfo
        locationTitleText.text = mapSearchInfo.fullAddress

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    mapSearchInfo.locationLatLng.latitude,
                    mapSearchInfo.locationLatLng.longitude
                ), CAMERA_ZOOM_LEVEL
            )
        )

        map.setOnCameraIdleListener {
            if (!isLocationChaned) {
                isLocationChaned = true
                Handler(Looper.getMainLooper()).postDelayed({
                    val cameraLatLng = map.cameraPosition.target
                    viewModel.changeLocationInfo(
                        LocationLatLngEntity(
                            latitude = cameraLatLng.latitude,
                            longitude = cameraLatLng.longitude
                        )
                    )
                    isLocationChaned = false
                }, 1000)
            }
        }
    }

    companion object {
        fun newIntent(context: Context, mapSearchInfoEntity: MapSearchInfoEntity) =
            Intent(context, MyLocationActivity::class.java).apply {
                putExtra(HomeViewModel.MY_LOCATION_KEY, mapSearchInfoEntity)
            }

        private const val CAMERA_ZOOM_LEVEL = 17f
    }
}