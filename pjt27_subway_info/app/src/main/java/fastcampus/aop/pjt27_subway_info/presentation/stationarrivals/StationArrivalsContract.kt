package fastcampus.aop.pjt27_subway_info.presentation.stationarrivals

import fastcampus.aop.pjt27_subway_info.domain.ArrivalInformation
import fastcampus.aop.pjt27_subway_info.domain.Station
import fastcampus.aop.pjt27_subway_info.presentation.BasePresenter
import fastcampus.aop.pjt27_subway_info.presentation.BaseView

interface StationArrivalsContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStationArrivals(arrivalInformation: List<ArrivalInformation>)

        fun showErrorDescription(message: String)
    }

    interface Presenter: BasePresenter {
        fun fetchStationArrivals()

        fun toggleStationFavorite()
    }
}