package fastcampus.aop.pjt28_delivery_info.presentation.addtrackingitem

import fastcampus.aop.pjt28_delivery_info.data.entity.ShippingCompany
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.presentation.BasePresenter
import fastcampus.aop.pjt28_delivery_info.presentation.BaseView

class AddTrackingItemContract {

    interface View: BaseView<Presenter> {
        fun showShippingCompaniesLoadingIndicator()

        fun hideShippingCompaniesLoadingIndicator()

        fun showRecommendCompanyLoadingIndicator()

        fun hideRecommendCompanyLoadingIndicator()

        fun showSaveTrackingItemIndicator()

        fun hideSaveTackingItemIndicator()

        fun showCompanies(companies: List<ShippingCompany>)

        fun showRecommendCompany(company: ShippingCompany)

        fun enableSaveButton()

        fun disableSaveButton()

        fun showErrorToast(message: String)

        fun finish()
    }

    interface Presenter: BasePresenter {

        var invoice: String?
        var shippingCompanies: List<ShippingCompany>?
        var selectedShippingCompany: ShippingCompany?

        fun fetchShippingCompanies()

        fun fetchRecommendShippingCompany()

        fun changeSelectedShippingCompany(companyName: String)

        fun changeShippingInvoice(invoice: String)

        fun saveTrackingItem()
    }
}