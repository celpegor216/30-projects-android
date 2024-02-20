package fastcampus.aop.pjt27_subway_info.data.api

import fastcampus.aop.pjt27_subway_info.data.db.entity.StationEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.SubwayEntity

interface StationApi {

    suspend fun getStationDataUpdatedTimeMillis(): Long

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}