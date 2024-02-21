package fastcampus.aop.pjt28_delivery_info.presentation.trackinghistory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt28_delivery_info.data.entity.TrackingDetail
import fastcampus.aop.pjt28_delivery_info.databinding.ItemTrackingHistoryBinding

class TrackingHistoryAdapter: RecyclerView.Adapter<TrackingHistoryAdapter.ViewHolder>() {

    var data: List<TrackingDetail> = emptyList()

    inner class ViewHolder(
        private val binding: ItemTrackingHistoryBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(trackingDetail: TrackingDetail) {
            binding.timeStampTextView.text = trackingDetail.timeString
            binding.stateTextView.text = trackingDetail.kind
            binding.locationTextView.text = "@${trackingDetail.where}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemTrackingHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size
}