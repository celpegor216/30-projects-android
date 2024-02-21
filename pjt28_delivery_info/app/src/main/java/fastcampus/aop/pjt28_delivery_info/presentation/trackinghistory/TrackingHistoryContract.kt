package fastcampus.aop.pjt28_delivery_info.presentation.trackinghistory

import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.presentation.BasePresenter
import fastcampus.aop.pjt28_delivery_info.presentation.BaseView

class TrackingHistoryContract {

    interface View: BaseView<Presenter> {

        fun hideLoadingIndicator()

        fun showTrackingItemInformation(trackingItem: TrackingItem, trackingInformation: TrackingInformation)

        fun finish()
    }

    interface Presenter: BasePresenter {

        fun refresh()

        fun deleteTrackingItem()
    }
}