package fastcampus.aop.pjt30_food_delivery.screen.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseFragment<VM: BaseViewModel, VB: ViewBinding>: Fragment() {

    abstract val viewModel: VM

    // ViewModel에서 정의한 fetchData를 lifecycle에 맞게 처리
    private lateinit var fetchJob: Job

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initState()
    }

    open fun initState() {
        arguments?.let {
            viewModel.storeState(it)
        }

        initViews()
        fetchJob = viewModel.fetchData()
        observeData()
    }

    open fun initViews() {}

    abstract fun observeData()

    override fun onDestroy() {
        super.onDestroy()

        if (fetchJob.isActive) {
            fetchJob.cancel()
        }
    }
}