package fastcampus.aop.pjt18_map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt18_map.databinding.ViewholderSearchResultItemBinding
import fastcampus.aop.pjt18_map.model.SearchResultEntity

class SearchRecyclerAdapter: RecyclerView.Adapter<SearchRecyclerAdapter.SearchResultItemViewHolder>() {

    private lateinit var searchResultClickListener: (SearchResultEntity) -> Unit
    private var searchResultList: List<SearchResultEntity> = emptyList()

    inner class SearchResultItemViewHolder(
        private val binding: ViewholderSearchResultItemBinding,
        val searchResultClickListener: (SearchResultEntity) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SearchResultEntity) {
            with(binding) {
                nameTextView.text = data.name
                fullAddressTextView.text = data.fullAddress
                root.setOnClickListener {
                    searchResultClickListener(data)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: SearchResultItemViewHolder, position: Int) {
        return holder.bind(searchResultList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultItemViewHolder {
        val view = ViewholderSearchResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultItemViewHolder(view, searchResultClickListener)
    }

    override fun getItemCount(): Int = searchResultList.size

    fun setSearchResultList(searchResultList: List<SearchResultEntity>, searchResultClickListener: (SearchResultEntity) -> Unit) {
        this.searchResultList = searchResultList
        this.searchResultClickListener = searchResultClickListener
        notifyDataSetChanged()
    }
}