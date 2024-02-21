package fastcampus.aop.pjt28_delivery_info.presentation.addtrackingitem

import fastcampus.aop.pjt28_delivery_info.data.entity.ShippingCompany
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.data.repository.ShippingCompanyRepository
import fastcampus.aop.pjt28_delivery_info.data.repository.TrackingItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AddTrackingItemPresenter(
    private val view: AddTrackingItemContract.View,
    private val shippingCompanyRepository: ShippingCompanyRepository,
    private val trackingItemRepository: TrackingItemRepository
): AddTrackingItemContract.Presenter {

    override val scope: CoroutineScope
        get() = MainScope()

    override var invoice: String? = null
    override var shippingCompanies: List<ShippingCompany>? = null
    override var selectedShippingCompany: ShippingCompany? = null

    override fun onViewCreated() {
        fetchShippingCompanies()
    }

    override fun onDestroyView() {}

    override fun fetchShippingCompanies() {
        scope.launch {
            view.showShippingCompaniesLoadingIndicator()

            if (shippingCompanies.isNullOrEmpty()) {
                shippingCompanies = shippingCompanyRepository.getShippingCompanies()
            }

            shippingCompanies?.let {
                view.showCompanies(it)
            }

            view.hideShippingCompaniesLoadingIndicator()
        }
    }

    override fun fetchRecommendShippingCompany() {
        scope.launch {
            view.showRecommendCompanyLoadingIndicator()
            shippingCompanyRepository.getRecommendShippingCompany(invoice!!)?.let { view.showRecommendCompany(it) }
            view.hideRecommendCompanyLoadingIndicator()
        }
    }

    override fun changeSelectedShippingCompany(companyName: String) {
        selectedShippingCompany = shippingCompanies?.find { it.name == companyName }
        enableSaveButtonIfAvailable()
    }

    override fun changeShippingInvoice(invoice: String) {
        this.invoice = invoice
        enableSaveButtonIfAvailable()
    }

    override fun saveTrackingItem() {
        scope.launch {
            try {
                view.showSaveTrackingItemIndicator()

                trackingItemRepository.saveTrackingItem(
                    TrackingItem(
                        invoice!!,
                        selectedShippingCompany!!
                    )
                )

                view.finish()
            } catch (e: Exception) {
                view.showErrorToast(e.message ?: "오류가 발생하여 운송장 추가에 실패했습니다.")
            } finally {
                view.hideSaveTackingItemIndicator()
            }
        }
    }

    private fun enableSaveButtonIfAvailable() {
        if (!invoice.isNullOrBlank() && selectedShippingCompany != null) {
            view.enableSaveButton()
        } else {
            view.disableSaveButton()
        }
    }
}