package fastcampus.aop.pjt27_subway_info.data.repository

import fastcampus.aop.pjt27_subway_info.data.api.StationApi
import fastcampus.aop.pjt27_subway_info.data.api.StationArrivalsApi
import fastcampus.aop.pjt27_subway_info.data.api.response.mapper.toArrivalInformation
import fastcampus.aop.pjt27_subway_info.data.db.StationDao
import fastcampus.aop.pjt27_subway_info.data.db.entity.StationSubwayCrossRefEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.mapper.toStationEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.mapper.toStations
import fastcampus.aop.pjt27_subway_info.data.preference.PreferenceManager
import fastcampus.aop.pjt27_subway_info.domain.ArrivalInformation
import fastcampus.aop.pjt27_subway_info.domain.Station
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class StationRepositoryImpl(
    private val stationApi: StationApi,
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : StationRepository {

    override val stations: Flow<List<Station>> =
        stationDao.getStationWithSubways()
            .distinctUntilChanged()    // 쿼리와 관련없는 변경에도 호출됨을 방지하기 위함
            .map { stations -> stations.toStations().sortedByDescending { it.isFavorited } }
            .flowOn(dispatcher)

    override suspend fun refreshStations() = withContext(dispatcher) {
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis =
            preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis) {
            stationDao.insertStationSubways(stationApi.getStationSubways())
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, fileUpdatedTimeMillis)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher) {
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy { it.direction }
            ?.sortedBy { it.subway }
            ?: throw RuntimeException("도착 정보를 불러오는 데 실패했습니다.")
    }

    override suspend fun updateStation(station: Station) {
        stationDao.updateStation(station.toStationEntity())
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS =
            "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}