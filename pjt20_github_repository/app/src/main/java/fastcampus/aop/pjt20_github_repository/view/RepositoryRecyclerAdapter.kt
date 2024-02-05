package fastcampus.aop.pjt20_github_repository.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity
import fastcampus.aop.pjt20_github_repository.databinding.RepositoryItemBinding
import fastcampus.aop.pjt20_github_repository.extensions.loadCenterInside

class RepositoryRecyclerAdapter: RecyclerView.Adapter<RepositoryRecyclerAdapter.ViewHolder>() {

    private var repositoryList: List<GithubRepoEntity> = listOf()
    private lateinit var repositoryClickListener: (GithubRepoEntity) -> Unit

    inner class ViewHolder (
        private val binding: RepositoryItemBinding,
        val searchResultClickListener: (GithubRepoEntity) -> Unit
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(data: GithubRepoEntity) = with(binding) {
            ownerProfileImageView.loadCenterInside(data.owner.avatarUrl, 24f)
            ownerNameTextView.text = data.owner.login
            nameTextView.text = data.fullName
            subtextTextView.text = data.description
            stargazersCountTextView.text = data.stargazersCount.toString()
            data.language?.let { language ->
                languageTextView.isGone = false
                languageTextView.text = language
            } ?: kotlin.run {
                languageTextView.isGone = true
                languageTextView.text = ""
            }

            root.setOnClickListener {
                searchResultClickListener(data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RepositoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), repositoryClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(repositoryList[position])
    }

    override fun getItemCount(): Int = repositoryList.size

    fun setSearchResultList(searchResultList: List<GithubRepoEntity>, searchResultClickListener: (GithubRepoEntity) -> Unit) {
        this.repositoryList = searchResultList
        this.repositoryClickListener = searchResultClickListener
        notifyDataSetChanged()
    }
}