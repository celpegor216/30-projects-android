package fastcampus.aop.pjt30_food_delivery.widget.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt30_food_delivery.databinding.ViewholderPhotoImageBinding
import fastcampus.aop.pjt30_food_delivery.extension.load

class PhotoListAdapter(
    private val removePhotoListener: (Uri) -> Unit
): RecyclerView.Adapter<PhotoListAdapter.PhotoItemViewHolder>() {

    private var imageUriList: List<Uri> = listOf()

    inner class PhotoItemViewHolder(
        private val binding: ViewholderPhotoImageBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Uri) = with(binding) {
            photoImageView.load(data.toString(), 8f)

            closeButton.setOnClickListener {
                removePhotoListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoItemViewHolder {
        return PhotoItemViewHolder(ViewholderPhotoImageBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PhotoItemViewHolder, position: Int) {
        holder.bind(imageUriList[position])
    }

    override fun getItemCount(): Int = imageUriList.size

    fun setPhotoList(imageUriList: List<Uri>) {
        this.imageUriList = imageUriList
        notifyDataSetChanged()
    }
}