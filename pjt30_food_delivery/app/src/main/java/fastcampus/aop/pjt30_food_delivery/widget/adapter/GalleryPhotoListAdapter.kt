package fastcampus.aop.pjt30_food_delivery.widget.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt30_food_delivery.R
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderGalleryPhotoBinding
import fastcampus.aop.pjt30_food_delivery.extension.load
import fastcampus.aop.pjt30_food_delivery.data.entity.GalleryPhoto

class GalleryPhotoListAdapter(
    private val checkPhotoListener: (GalleryPhoto) -> Unit
): RecyclerView.Adapter<GalleryPhotoListAdapter.GalleryPhotoItemViewHolder>() {

    private var galleryPhotoList: List<GalleryPhoto> = listOf()

    inner class GalleryPhotoItemViewHolder(
        private val binding: ViewholderGalleryPhotoBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GalleryPhoto) = with(binding) {
            photoImageView.load(data.uri.toString(), 8f)

            checkButton.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context,
                    if (data.isSelected)
                        R.drawable.ic_check_enabled
                    else
                        R.drawable.ic_check_disabled
                )
            )

            root.setOnClickListener {
                checkPhotoListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPhotoItemViewHolder {
        return GalleryPhotoItemViewHolder(
            ViewholderGalleryPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GalleryPhotoItemViewHolder, position: Int) {
        holder.bind(galleryPhotoList[position])
    }

    override fun getItemCount(): Int = galleryPhotoList.size

    fun setGalleryPhotoList(galleryPhotoList: List<GalleryPhoto>) {
        this.galleryPhotoList = galleryPhotoList
        notifyDataSetChanged()
    }

}