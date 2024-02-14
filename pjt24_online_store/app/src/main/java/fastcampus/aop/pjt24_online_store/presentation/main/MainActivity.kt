package fastcampus.aop.pjt24_online_store.presentation.main

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fastcampus.aop.pjt24_online_store.R
import fastcampus.aop.pjt24_online_store.databinding.ActivityMainBinding
import fastcampus.aop.pjt24_online_store.presentation.BaseActivity
import fastcampus.aop.pjt24_online_store.presentation.BaseFragment
import fastcampus.aop.pjt24_online_store.presentation.list.ProductListFragment
import fastcampus.aop.pjt24_online_store.presentation.profile.ProfileFragment
import org.koin.android.ext.android.inject

internal class MainActivity
    : BaseActivity<MainViewModel, ActivityMainBinding>(), BottomNavigationView.OnNavigationItemSelectedListener
{

    override val viewModel by inject<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() = with(binding) {
        bottomNavigation.setOnNavigationItemSelectedListener(this@MainActivity)
        showFragment(ProductListFragment(), ProductListFragment.TAG)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_products -> {
                showFragment(ProductListFragment(), ProductListFragment.TAG)
                true
            }
            R.id.menu_profile -> {
                showFragment(ProfileFragment(), ProfileFragment.TAG)
                true
            }
            else -> false
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)
        supportFragmentManager.fragments.forEach { fm ->
            supportFragmentManager.beginTransaction().hide(fm).commit()
        }
        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commit()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()    // 화면 회전 시 발생할 수 있는 데이터 손실 허용
        }
    }

    override fun observeData() = viewModel.mainStateLiveData.observe(this) {
        when (it) {
            is MainState.RefreshOrderList -> {
                binding.bottomNavigation.selectedItemId = R.id.menu_profile
                val fragment = supportFragmentManager.findFragmentByTag(ProfileFragment.TAG)
                (fragment as BaseFragment<*, *>)?.viewModel?.fetchData()
            }
        }
    }
}