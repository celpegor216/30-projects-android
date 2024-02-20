package fastcampus.aop.pjt27_subway_info.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import fastcampus.aop.pjt27_subway_info.data.db.entity.StationEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.StationSubwayCrossRefEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.StationWithSubwaysEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.SubwayEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StationDao {

    // Flow: Coroutines의 suspend와 다르게 observable, LiveData와 유사
    @Transaction
    @Query("SELECT * FROM StationEntity")
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<StationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(references: List<StationSubwayCrossRefEntity>)

    // Transaction으로 묶어서 오류가 발생했을 때 전체 롤백되도록 처리하는 게 안전
    @Transaction
    suspend fun insertStationSubways(stationSubways: List<Pair<StationEntity, SubwayEntity>>) {
        insertStations(stationSubways.map { it.first })
        insertSubways(stationSubways.map { it.second })
        insertCrossReferences(
            stationSubways.map { (station, subway) ->
                StationSubwayCrossRefEntity(
                    station.stationName,
                    subway.subwayId
                )
            }
        )
    }

    @Update
    suspend fun updateStation(station: StationEntity)
}
