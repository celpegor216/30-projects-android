package fastcampus.aop.pjt20_github_repository

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isGone
import fastcampus.aop.pjt20_github_repository.data.database.DataBaseProvider
import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity
import fastcampus.aop.pjt20_github_repository.databinding.ActivityMainBinding
import fastcampus.aop.pjt20_github_repository.view.RepositoryRecyclerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RepositoryRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        initViews()
    }

    private fun initAdapter() {
        adapter = RepositoryRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        recyclerView.adapter = adapter

        searchButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        launch(coroutineContext) {
            loadLikedRepositoryList()
        }
    }

    private suspend fun loadLikedRepositoryList() = withContext(Dispatchers.IO) {
        val repoList = DataBaseProvider.provideDB(this@MainActivity).repositoryDao().getAllRepository()
        withContext(Dispatchers.Main) {
            setData(repoList)
        }
    }

    private fun setData(repoList: List<GithubRepoEntity>) = with(binding) {
        if (repoList.isEmpty()) {
            emptyResultTextView.isGone = false
            recyclerView.isGone = true
        } else {
            emptyResultTextView.isGone = true
            recyclerView.isGone = false
            adapter.setSearchResultList(repoList) {
                startActivity(Intent(this@MainActivity, RepositoryActivity::class.java).apply {
                    putExtra(RepositoryActivity.REPOSITORY_OWNER_KEY, it.owner.login)
                    putExtra(RepositoryActivity.REPOSITORY_NAME_KEY, it.name)
                })
            }
        }
    }
}