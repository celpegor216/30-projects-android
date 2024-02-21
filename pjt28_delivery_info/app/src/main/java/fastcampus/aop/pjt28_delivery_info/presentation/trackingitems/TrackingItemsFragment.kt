package fastcampus.aop.pjt28_delivery_info.presentation.trackingitems

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt28_delivery_info.R
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.databinding.FragmentTrackingItemsBinding
import fastcampus.aop.pjt28_delivery_info.extension.toGone
import fastcampus.aop.pjt28_delivery_info.extension.toVisible
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment

class TrackingItemsFragment : ScopeFragment(), TrackingItemsContract.View {

    override val presenter: TrackingItemsContract.Presenter by inject()

    private var binding: FragmentTrackingItemsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTrackingItemsBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        bindViews()
        presenter.onViewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showLoadingIndicator() {
        binding?.progressBar?.toVisible()
    }

    override fun hideLoadingIndicator() {
        binding?.progressBar?.toGone()
        binding?.refreshLayout?.isRefreshing = false
    }

    override fun showNoDataDescription() {
        binding?.refreshLayout?.toGone()
        binding?.noDataContainer?.toVisible()
    }

    override fun showTrackingItemInformation(trackingInformation: List<Pair<TrackingItem, TrackingInformation>>) {
        binding?.refreshLayout?.toVisible()
        binding?.noDataContainer?.toGone()
        (binding?.recyclerView?.adapter as? TrackingItemsAdapter)?.apply {
            this.data = trackingInformation
            notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = TrackingItemsAdapter()
        }
    }

    private fun bindViews() {
        binding?.refreshLayout?.setOnRefreshListener {
            presenter.refresh()
        }

        binding?.addTrackingItemButton?.setOnClickListener {
            findNavController().navigate(R.id.to_add_tracking_item)
        }

        binding?.addTrackingItemFAB?.setOnClickListener {
            findNavController().navigate(R.id.to_add_tracking_item)
        }

        (binding?.recyclerView?.adapter as? TrackingItemsAdapter)?.onClickItemListener = { item, information ->
            findNavController().navigate(
                TrackingItemsFragmentDirections.toTrackingHistory(item, information)
            )
        }
    }
}