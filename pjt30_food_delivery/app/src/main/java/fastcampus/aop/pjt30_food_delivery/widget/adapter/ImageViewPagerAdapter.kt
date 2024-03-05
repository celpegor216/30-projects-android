package fastcampus.aop.pjt30_food_delivery.widget.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderImageBinding
import fastcampus.aop.pjt30_food_delivery.extension.load

class ImageViewPagerAdapter(
    var uriList: List<Uri>
): RecyclerView.Adapter<ImageViewPagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(
        private val binding: ViewholderImageBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) = with(binding) {
            imageView.load(uri.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(ViewholderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(uriList[position])
    }

    override fun getItemCount(): Int = uriList.size
}