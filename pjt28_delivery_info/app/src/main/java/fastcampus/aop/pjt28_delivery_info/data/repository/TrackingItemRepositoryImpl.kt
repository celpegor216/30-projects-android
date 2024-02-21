package fastcampus.aop.pjt28_delivery_info.data.repository

import fastcampus.aop.pjt28_delivery_info.data.api.SweetTrackerApi
import fastcampus.aop.pjt28_delivery_info.data.db.TrackingItemDao
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class TrackingItemRepositoryImpl(
    private val sweetTrackerApi: SweetTrackerApi,
    private val trackingItemDao: TrackingItemDao,
    private val dispatcher: CoroutineDispatcher
) : TrackingItemRepository {

    override val trackingItems: Flow<List<TrackingItem>> = trackingItemDao.allTrackingItems().distinctUntilChanged()

    override suspend fun getTrackingItemsInformation(): List<Pair<TrackingItem, TrackingInformation>> =
        withContext(dispatcher) {
            trackingItemDao.getAll().mapNotNull { trackingItem ->
                val relatedTrackingInfo = sweetTrackerApi.getTrackingInformation(
                    trackingItem.company.code,
                    trackingItem.invoice
                ).body()?.sortTrackingDetailsByTimeDescending()

                if (!relatedTrackingInfo!!.errorMessage.isNullOrBlank()) {
                    null
                } else {
                    trackingItem to relatedTrackingInfo
                }
            }
                .sortedWith(
                    compareBy(
                        { it.second.level },
                        { -(it.second.lastDetail?.time ?: Long.MAX_VALUE) }
                    )
                )
        }

    override suspend fun getTrackingInformation(
        companyCode: String,
        invoice: String
    ): TrackingInformation? =
        sweetTrackerApi.getTrackingInformation(companyCode, invoice).body()
            ?.sortTrackingDetailsByTimeDescending()

    override suspend fun saveTrackingItem(trackingItem: TrackingItem) = withContext(dispatcher) {
        val trackingInformation = sweetTrackerApi.getTrackingInformation(
            trackingItem.company.code,
            trackingItem.invoice
        ).body()

        if (!trackingInformation!!.errorMessage.isNullOrBlank()) {
            throw RuntimeException(trackingInformation.errorMessage)
        }

        trackingItemDao.insert(trackingItem)
    }

    override suspend fun deleteTrackingItem(trackingItem: TrackingItem) {
        trackingItemDao.delete(trackingItem)
    }

    private fun TrackingInformation.sortTrackingDetailsByTimeDescending() =
        copy(trackingDetails = trackingDetails?.sortedByDescending { it?.time ?: 0L })
}