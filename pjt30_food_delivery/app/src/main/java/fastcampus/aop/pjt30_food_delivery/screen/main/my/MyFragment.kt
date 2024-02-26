package fastcampus.aop.pjt30_food_delivery.screen.main.my

import fastcampus.aop.pjt30_food_delivery.databinding.FragmentMyBinding
import fastcampus.aop.pjt30_food_delivery.screen.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyFragment: BaseFragment<MyViewModel, FragmentMyBinding>() {

    override val viewModel by viewModel<MyViewModel>()

    override fun getViewBinding(): FragmentMyBinding =
        FragmentMyBinding.inflate(layoutInflater)

    override fun observeData() {}

    companion object {
        fun newInstance() = MyFragment()
        const val TAG = "MyFragment"
    }
}