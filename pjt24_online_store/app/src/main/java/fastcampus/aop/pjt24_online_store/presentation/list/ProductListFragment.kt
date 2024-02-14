package fastcampus.aop.pjt24_online_store.presentation.list

import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isGone
import fastcampus.aop.pjt24_online_store.databinding.FragmentProductListBinding
import fastcampus.aop.pjt24_online_store.extensions.toast
import fastcampus.aop.pjt24_online_store.presentation.BaseFragment
import fastcampus.aop.pjt24_online_store.presentation.adapter.ProductListAdapter
import fastcampus.aop.pjt24_online_store.presentation.detail.ProductDetailActivity
import fastcampus.aop.pjt24_online_store.presentation.main.MainActivity
import org.koin.android.ext.android.inject

internal class ProductListFragment: BaseFragment<ProductListViewModel, FragmentProductListBinding>() {

    override val viewModel by inject<ProductListViewModel>()

    override fun getViewBinding(): FragmentProductListBinding = FragmentProductListBinding.inflate(layoutInflater)

    private val adapter = ProductListAdapter()

    private val startProductDetailForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == ProductDetailActivity.PRODUCT_ORDERED_RESULT_CODE) {
                (requireActivity() as MainActivity).viewModel.refreshOrderList()
            }
        }

    override fun observeData() = viewModel.productListStateLiveData.observe(this) {
        when (it) {
            is ProductListState.Uninitialized -> {
                initViews()
            }
            is ProductListState.Loading -> {
                handleLoadingState()
            }
            is ProductListState.Success -> {
                handleSuccessState(it)
            }
            is ProductListState.Error -> {
                handleErrorState()
            }
        }
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        refreshLayout.setOnRefreshListener {
            viewModel.fetchData()
        }
    }

    private fun handleLoadingState() = with(binding) {
        refreshLayout.isRefreshing = true
    }

    private fun handleSuccessState(state: ProductListState.Success) = with(binding) {
        refreshLayout.isEnabled = state.productList.isNotEmpty()
        refreshLayout.isRefreshing = false

        if (state.productList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false

            adapter.setProductList(state.productList) {
                startProductDetailForResult.launch(
                    ProductDetailActivity.newIntent(requireContext(), it.id)
                )
            }
        }
    }

    private fun handleErrorState() {
        requireContext().toast("에러가 발생했습니다.")
    }

    companion object {
        const val TAG = "ProductListFragment"
    }
}