package fastcampus.aop.pjt27_subway_info.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import fastcampus.aop.pjt27_subway_info.R
import fastcampus.aop.pjt27_subway_info.databinding.ActivityMainBinding
import fastcampus.aop.pjt27_subway_info.extension.toGone
import fastcampus.aop.pjt27_subway_info.extension.toVisible
import fastcampus.aop.pjt27_subway_info.presentation.stationarrivals.StationArrivalsFragmentArgs

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val navigationController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        initViews()
        bindViews()
    }

    // actionbar와 navigation controller 연동
    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initViews() {
        // toolbar가 actionbar로 동작하도록 처리
        setSupportActionBar(binding.toolbar)

        // actionbar와 navigation controller 연동
        // navigation back 버튼 등이 자동으로 노출됨
        setupActionBarWithNavController(navigationController)
    }

    private fun bindViews() {
        navigationController.addOnDestinationChangedListener { _, destination, argument ->
            if (destination.id == R.id.station_arrivals_dest) {
                title = StationArrivalsFragmentArgs.fromBundle(argument!!).station.name
                binding.toolbar.toVisible()
            } else {
                binding.toolbar.toGone()
            }
        }
    }
}