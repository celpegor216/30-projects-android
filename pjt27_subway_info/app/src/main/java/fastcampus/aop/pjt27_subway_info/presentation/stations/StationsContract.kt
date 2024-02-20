package fastcampus.aop.pjt27_subway_info.presentation.stations

import fastcampus.aop.pjt27_subway_info.domain.Station
import fastcampus.aop.pjt27_subway_info.presentation.BasePresenter
import fastcampus.aop.pjt27_subway_info.presentation.BaseView

interface StationsContract {

    interface View: BaseView<Presenter> {
        fun showLoadingIndicator()

        fun hideLoadingIndicator()

        fun showStations(stations: List<Station>)
    }

    interface Presenter: BasePresenter {
        fun filterStations(query: String)

        fun toggleStationFavorite(station: Station)
    }
}