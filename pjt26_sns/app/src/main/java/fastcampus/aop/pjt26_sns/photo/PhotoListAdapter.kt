package fastcampus.aop.pjt26_sns.photo

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt26_sns.databinding.ViewholderPhotoImageBinding
import fastcampus.aop.pjt26_sns.extensions.loadCenterCrop

class PhotoListAdapter(
    private val removePhotoListener: (Uri) -> Unit
): RecyclerView.Adapter<PhotoListAdapter.PhotoItemViewHolder>() {

    private var imageUriList: List<Uri> = listOf()

    inner class PhotoItemViewHolder(
        private val binding: ViewholderPhotoImageBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Uri) = with(binding) {
            photoImageView.loadCenterCrop(data.toString(), 8f)

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