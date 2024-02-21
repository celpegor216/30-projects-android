package fastcampus.aop.pjt28_delivery_info.presentation.trackinghistory

import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TrackingHistoryPresenter(
    private val view: TrackingHistoryContract.View,
    private val trackingItemRepository: TrackingItemRepository,
    private val trackingItem: TrackingItem,
    private var trackingInformation: TrackingInformation
): TrackingHistoryContract.Presenter {

    override val scope: CoroutineScope
        get() = MainScope()

    override fun onViewCreated() {
        view.showTrackingItemInformation(trackingItem, trackingInformation)
    }

    override fun onDestroyView() {}

    override fun refresh() {
        scope.launch {
            try {
                val newTrackingInformation =
                    trackingItemRepository.getTrackingInformation(
                        trackingItem.company.code,
                        trackingItem.invoice
                    )
                newTrackingInformation?.let {
                    trackingInformation = it
                    view.showTrackingItemInformation(trackingItem, trackingInformation)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                view.hideLoadingIndicator()
            }
        }
    }

    override fun deleteTrackingItem() {
        scope.launch {
            try {
                trackingItemRepository.deleteTrackingItem(trackingItem)
                view.finish()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}