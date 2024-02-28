package fastcampus.aop.pjt30_food_delivery.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListFragment

class RestaurantDetailListFragmentPagerAdapter(
    private val activity: FragmentActivity,
    private var fragmentList: List<Fragment>
): FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment =
        fragmentList[position]
}