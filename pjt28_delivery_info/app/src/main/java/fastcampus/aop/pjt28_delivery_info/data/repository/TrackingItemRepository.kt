package fastcampus.aop.pjt28_delivery_info.data.repository

import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import kotlinx.coroutines.flow.Flow

interface TrackingItemRepository {

    val trackingItems: Flow<List<TrackingItem>>

    suspend fun getTrackingItemsInformation(): List<Pair<TrackingItem, TrackingInformation>>

    suspend fun getTrackingInformation(
        companyCode: String,
        invoice: String
    ): TrackingInformation?

    suspend fun saveTrackingItem(trackingItem: TrackingItem)

    suspend fun deleteTrackingItem(trackingItem: TrackingItem)
}