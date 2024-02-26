package fastcampus.aop.pjt30_food_delivery.screen.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Job

abstract class BaseActivity<VM: BaseViewModel, VB: ViewBinding>: AppCompatActivity() {

    abstract val viewModel: VM

    // ViewModel에서 정의한 fetchData를 lifecycle에 맞게 처리
    private lateinit var fetchJob: Job

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getViewBinding()
        setContentView(binding.root)
        initState()
    }

    open fun initState() {
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