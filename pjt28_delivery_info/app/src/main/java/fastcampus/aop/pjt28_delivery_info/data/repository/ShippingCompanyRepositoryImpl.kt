package fastcampus.aop.pjt28_delivery_info.data.repository

import fastcampus.aop.pjt28_delivery_info.data.api.SweetTrackerApi
import fastcampus.aop.pjt28_delivery_info.data.db.ShippingCompanyDao
import fastcampus.aop.pjt28_delivery_info.data.entity.ShippingCompany
import fastcampus.aop.pjt28_delivery_info.data.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class ShippingCompanyRepositoryImpl(
    private val sweetTrackerApi: SweetTrackerApi,
    private val shippingCompanyDao: ShippingCompanyDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : ShippingCompanyRepository {

    override suspend fun getShippingCompanies(): List<ShippingCompany> = withContext(dispatcher) {
        val currentTimeMillis = System.currentTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if (lastDatabaseUpdatedTimeMillis == null || CACHE_MAX_AGE_MILLIS < (currentTimeMillis - lastDatabaseUpdatedTimeMillis)) {
            val shippingCompanies = sweetTrackerApi.getShippingCompanies().body()
                ?.shippingCompanies
                ?: emptyList()

            shippingCompanyDao.insertAll(shippingCompanies)
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS, currentTimeMillis)
        }

        shippingCompanyDao.getAll()
    }

    override suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany? = withContext(dispatcher) {
        try {
            sweetTrackerApi.getRecommendShippingCompanies(invoice).body()
                ?.shippingCompanies
                ?.minByOrNull { it.code.toIntOrNull() ?: Int.MAX_VALUE }
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS = "KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60 * 60 * 24 * 7
    }
}