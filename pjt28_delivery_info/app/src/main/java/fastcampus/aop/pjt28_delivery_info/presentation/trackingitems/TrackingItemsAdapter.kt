package fastcampus.aop.pjt28_delivery_info.presentation.trackingitems

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt28_delivery_info.R
import fastcampus.aop.pjt28_delivery_info.data.entity.Level
import fastcampus.aop.pjt28_delivery_info.data.entity.ShippingCompany
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingInformation
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingItem
import fastcampus.aop.pjt28_delivery_info.databinding.ItemTrackingBinding
import fastcampus.aop.pjt28_delivery_info.extension.setTextColorRes
import fastcampus.aop.pjt28_delivery_info.extension.toReadableDateString
import java.util.Date

class TrackingItemsAdapter : RecyclerView.Adapter<TrackingItemsAdapter.ViewHolder>() {

    var data: List<Pair<TrackingItem, TrackingInformation>> = emptyList()
    var onClickItemListener: ((TrackingItem, TrackingInformation) -> Unit)? = null

    inner class ViewHolder(
        private val binding: ItemTrackingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                data.getOrNull(adapterPosition)?.let { (item, information) ->
                    onClickItemListener?.invoke(item, information)
                }
            }
        }

        fun bind(company: ShippingCompany, trackingInformation: TrackingInformation) {
            binding.updatedAtTextView.text = Date(
                trackingInformation.lastDetail?.time ?: System.currentTimeMillis()
            ).toReadableDateString()

            binding.levelLabelTextView.text = trackingInformation.level?.label
            when (trackingInformation.level) {
                Level.COMPLETE -> {
                    binding.levelLabelTextView.setTextColor(org.koin.android.R.attr.colorPrimary)
                    binding.root.alpha = 0.5f
                }

                Level.PREPARE -> {
                    binding.levelLabelTextView.setTextColorRes(R.color.orange)
                    binding.root.alpha = 1f
                }

                else -> {
                    binding.levelLabelTextView.setTextColorRes(R.color.green)
                    binding.root.alpha = 1f
                }
            }

            binding.invoiceTextView.text = trackingInformation.invoiceNo

            if (trackingInformation.itemName.isNullOrBlank()) {
                binding.itemNameTextView.text = "이름 없음"
                binding.itemNameTextView.setTextColorRes(R.color.gray)
            } else {
                binding.itemNameTextView.text = trackingInformation.itemName
                binding.itemNameTextView.setTextColorRes(R.color.black)
            }

            binding.lastStateTextView.text =
                trackingInformation.lastDetail?.let { it.kind + "@${it.where}" }

            binding.companyNameTextView.text = company.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemTrackingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (trackingItem, trackingInformation) = data[position]
        holder.bind(trackingItem.company, trackingInformation)
    }

    override fun getItemCount(): Int = data.size
}