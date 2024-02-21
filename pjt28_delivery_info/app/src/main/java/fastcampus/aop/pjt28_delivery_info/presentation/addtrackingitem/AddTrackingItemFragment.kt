package fastcampus.aop.pjt28_delivery_info.presentation.addtrackingitem

import android.app.Activity
import android.app.AlertDialog
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.google.android.material.internal.ViewUtils.hideKeyboard
import fastcampus.aop.pjt28_delivery_info.data.entity.ShippingCompany
import fastcampus.aop.pjt28_delivery_info.databinding.FragmentAddTrackingItemBinding
import fastcampus.aop.pjt28_delivery_info.extension.toGone
import fastcampus.aop.pjt28_delivery_info.extension.toVisible
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ScopeFragment

class AddTrackingItemFragment : ScopeFragment(), AddTrackingItemContract.View {

    override val presenter: AddTrackingItemContract.Presenter by inject()

    private var binding: FragmentAddTrackingItemBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAddTrackingItemBinding.inflate(inflater)
        .also { binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        presenter.onViewCreated()

        changeInvoiceIfAvailable()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        hideKeyboard()
        presenter.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.onDestroy()
    }

    override fun showShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toVisible()
    }

    override fun hideShippingCompaniesLoadingIndicator() {
        binding?.shippingCompanyProgressBar?.toGone()
    }

    override fun showRecommendCompanyLoadingIndicator() {
        binding?.recommendCompanyLoadingProgressBar?.toVisible()
    }

    override fun hideRecommendCompanyLoadingIndicator() {
        binding?.recommendCompanyLoadingProgressBar?.toGone()
    }

    override fun showSaveTrackingItemIndicator() {
        binding?.saveButton?.apply {
            text = null
            isEnabled = false
        }
        binding?.saveProgressBar?.toVisible()
    }

    override fun hideSaveTackingItemIndicator() {
        binding?.saveButton?.apply {
            text = "저장"
            isEnabled = true
        }
        binding?.saveProgressBar?.toGone()
    }

    override fun showCompanies(companies: List<ShippingCompany>) {
        companies.forEach { company ->
            binding?.chipGroup?.addView(
                Chip(context).apply {
                    text = company.name
                }
            )
        }
    }

    override fun showRecommendCompany(company: ShippingCompany) {
        binding?.chipGroup?.children
            ?.filterIsInstance(Chip::class.java)
            ?.forEach { chip ->
                if (chip.text == company.name) {
                    binding?.chipGroup?.apply { check(chip.id) }
                    return@forEach
                }
            }
    }

    override fun enableSaveButton() {
        binding?.saveButton?.isEnabled = true
    }

    override fun disableSaveButton() {
        binding?.saveButton?.isEnabled = false
    }

    override fun showErrorToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        findNavController().popBackStack()
    }

    private fun bindViews() {
        binding?.chipGroup?.setOnCheckedChangeListener { group, checkedId ->
            presenter.changeSelectedShippingCompany(group.findViewById<Chip>(checkedId).text.toString())
        }

        binding?.invoiceEditText?.addTextChangedListener { editable ->
            presenter.changeShippingInvoice(editable.toString())
        }

        binding?.saveButton?.setOnClickListener { _ ->
            presenter.saveTrackingItem()
        }
    }

    private fun changeInvoiceIfAvailable() {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val invoice = clipboard.plainTextClip()
        if (!invoice.isNullOrBlank()) {
            AlertDialog.Builder(requireActivity())
                .setTitle("클립 보드에 있는 $invoice 를\n운송장 번호로 추가하시겠습니까?")
                .setPositiveButton("네") { _, _ ->
                    binding?.invoiceEditText?.setText(invoice)
                    presenter.fetchRecommendShippingCompany()
                }
                .setNegativeButton("아니오") { _, _ -> }
                .create()
                .show()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }

    private fun ClipboardManager.plainTextClip(): String? =
        if (hasPrimaryClip() && (primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true)) {
            primaryClip?.getItemAt(0)?.text?.toString()
        } else {
            null
        }
}