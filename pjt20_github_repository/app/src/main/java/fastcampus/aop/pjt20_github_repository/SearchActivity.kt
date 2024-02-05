package fastcampus.aop.pjt20_github_repository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity
import fastcampus.aop.pjt20_github_repository.databinding.ActivitySearchBinding
import fastcampus.aop.pjt20_github_repository.utility.RetrofitUtil
import fastcampus.aop.pjt20_github_repository.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SearchActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RepositoryRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
        bindViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isGone = true
        recyclerView.adapter = adapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchBarEditText.text.toString())
        }
    }

    private fun searchKeyword(keyword: String) {
        showProgress(true)
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.githubApiService.searchRepositories(keyword)

                    if (response.isSuccessful) {
                        withContext(Dispatchers.Main) {
                            response.body()?.let { searchResponse ->
                                setData(searchResponse.items)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@SearchActivity, "검색 중 오류가 발생했습니다. : ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setData(items: List<GithubRepoEntity>) = with(binding) {
        showProgress(false)

        if (items.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false

            adapter.setSearchResultList(items) {
                startActivity(Intent(this@SearchActivity, RepositoryActivity::class.java).apply {
                    putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                    putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                })
            }
        }

    }

    private fun showProgress(isShown: Boolean) = with(binding) {
        progressBar.isGone = !isShown
    }
}