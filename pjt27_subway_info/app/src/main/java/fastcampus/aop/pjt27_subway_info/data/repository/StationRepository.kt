package fastcampus.aop.pjt27_subway_info.data.repository

import fastcampus.aop.pjt27_subway_info.domain.ArrivalInformation
import fastcampus.aop.pjt27_subway_info.domain.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {

    val stations: Flow<List<Station>>

    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String): List<ArrivalInformation>

    suspend fun updateStation(station: Station)
}