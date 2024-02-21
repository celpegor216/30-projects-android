package fastcampus.aop.pjt28_delivery_info.data.repository

import fastcampus.aop.pjt28_delivery_info.data.entity.ShippingCompany

interface ShippingCompanyRepository {

    suspend fun getShippingCompanies(): List<ShippingCompany>

    suspend fun getRecommendShippingCompany(invoice: String): ShippingCompany?
}