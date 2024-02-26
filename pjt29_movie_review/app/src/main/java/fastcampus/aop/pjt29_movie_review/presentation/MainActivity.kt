package fastcampus.aop.pjt29_movie_review.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import fastcampus.aop.pjt29_movie_review.R
import fastcampus.aop.pjt29_movie_review.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.mainNavigationHostContainer) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() {
        // 탭을 이동해도 스택이 쌓이지 않아 툴바에 뒤로가기 버튼이 표시되지 않음
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_dest, R.id.my_page_dest
            )
        )
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }
}