package fastcampus.aop.pjt27_subway_info.data.api

import com.google.firebase.storage.FirebaseStorage
import fastcampus.aop.pjt27_subway_info.data.db.entity.StationEntity
import fastcampus.aop.pjt27_subway_info.data.db.entity.SubwayEntity
import kotlinx.coroutines.tasks.await

class StationStorageApi(firebaseStorage: FirebaseStorage): StationApi {

    private val sheetReference = firebaseStorage.reference.child(STATION_DATA_FILE_NAME)

    // org.jetbrains.kotlinx:kotlinx-coroutines-play-services
    // 의존성을 추가하면 metadata에 onCompleteListener가 아닌 await()로 Task를 받아 코루틴스럽게 처리 가능
    override suspend fun getStationDataUpdatedTimeMillis(): Long = sheetReference.metadata.await().updatedTimeMillis

    override suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        return byteArray.decodeToString()
            .lines()
            .drop(1)    // 헤더 제외
            .map { it.split(",") }
            .map { StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }
    }

    companion object {
        private const val STATION_DATA_FILE_NAME = "station_data.csv"
    }
}