package fastcampus.aop.pjt27_subway_info.presentation.stationarrivals

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt27_subway_info.databinding.ItemArrivalBinding
import fastcampus.aop.pjt27_subway_info.domain.ArrivalInformation
import fastcampus.aop.pjt27_subway_info.domain.Station

class StationArrivalsAdapter : RecyclerView.Adapter<StationArrivalsAdapter.ViewHolder>() {

    var data: List<ArrivalInformation> = emptyList()

    inner class ViewHolder(
        private val binding: ItemArrivalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(arrival: ArrivalInformation) {
            binding.labelTextView.badgeColor = arrival.subway.color
            binding.labelTextView.text = "${arrival.subway.label} - ${arrival.direction}"
            binding.destinationTextView.text = "🚩 ${arrival.destination}"
            binding.arrivalMessageTextView.text = arrival.message
            binding.arrivalMessageTextView.setTextColor(
                if (arrival.message.contains("당역"))
                    Color.RED
                else
                    Color.DKGRAY
            )
            binding.updatedTimeTextView.text = "측정 시간: ${arrival.updatedAt}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemArrivalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(data[position])

    override fun getItemCount(): Int = data.size
}