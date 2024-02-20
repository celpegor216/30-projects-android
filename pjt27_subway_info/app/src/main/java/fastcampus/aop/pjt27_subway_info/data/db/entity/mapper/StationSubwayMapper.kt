package fastcampus.aop.pjt27_subway_info.data.db.entity.mapper

import fastcampus.aop.pjt27_subway_info.data.db.entity.StationEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.StationWithSubwaysEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.SubwayEntity
import fastcampus.aop.pjt27_subway_info.domain.Station
import fastcampus.aop.pjt27_subway_info.domain.Subway

fun StationWithSubwaysEntity.toStation() =
    Station(
        name = station.stationName,
        isFavorited = station.isFavorited,
        connectedSubways = subways.toSubways()
    )

fun List<StationWithSubwaysEntity>.toStations() = map { it.toStation() }

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId) }

fun Station.toStationEntity() = StationEntity(
    stationName = name,
    isFavorited = isFavorited
)