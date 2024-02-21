package fastcampus.aop.pjt28_delivery_info.presentation.trackingitems

import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.presentation.BasePresenter
import fastcampus.aop.pjt28_delivery_info.presentation.BaseView

class TrackingItemsContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showNoDataDescription()

        fun showTrackingItemInformation(trackingInformation: List<Pair<TrackingItem, TrackingInformation>>)
    }

    interface Presenter: BasePresenter {
        var trackingItemInformation: List<Pair<TrackingItem, TrackingInformation>>

        fun refresh()
    }
}