package fastcampus.aop.pjt20_github_repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import fastcampus.aop.pjt20_github_repository.data.database.DataBaseProvider
import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity
import fastcampus.aop.pjt20_github_repository.databinding.ActivityRepositoryBinding
import fastcampus.aop.pjt20_github_repository.extensions.loadCenterInside
import fastcampus.aop.pjt20_github_repository.utility.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class RepositoryActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivityRepositoryBinding

    private val repositoryDao by lazy {
        DataBaseProvider.provideDB(applicationContext).repositoryDao()
    }

    companion object {
        const val REPOSITORY_OWNER_KEY = "REPOSITORY_OWNER_KEY"
        const val REPOSITORY_NAME_KEY = "REPOSITORY_NAME_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repositoryOwner = intent.getStringExtra(REPOSITORY_OWNER_KEY) ?: kotlin.run {
            toast("Repository Owner 값이 없습니다.")
            finish()
            return
        }

        val repositoryName = intent.getStringExtra(REPOSITORY_NAME_KEY) ?: kotlin.run {
            toast("Repository Name 값이 없습니다.")
            finish()
            return
        }

        launch {
            loadRepository(repositoryOwner, repositoryName)?.let {
                setData(it)
            } ?: run {
                toast("Repository 정보가 없습니다.")
                finish()
            }
        }

        showProgress(true)
    }

    private suspend fun loadRepository(
        repositoryOwner: String,
        repositoryName: String
    ): GithubRepoEntity? =
        withContext(coroutineContext) {
            var repositoryEntity: GithubRepoEntity? = null
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.githubApiService.getRepository(
                    ownerLogin = repositoryOwner,
                    repoName = repositoryName
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        response.body()?.let { repo ->
                            repositoryEntity = repo
                        }
                    }
                }
            }
            repositoryEntity
        }

    private fun setData(data: GithubRepoEntity) = with(binding) {
        showProgress(false)
        ownerProfileImageView.loadCenterInside(data.owner.avatarUrl, 42f)
        ownerNameAndRepoNameTextView.text = "${data.owner.login}/${data.name}"
        stargazersCountTextView.text = data.stargazersCount.toString()
        data.language?.let { language ->
            languageTextView.isGone = false
            languageTextView.text = language
        } ?: kotlin.run {
            languageTextView.isGone = true
            languageTextView.text = ""
        }
        descriptionTextView.text = data.description
        updateTimeTextView.text = data.updatedAt

        setLikeState(data)
    }

    private fun setLikeState(githubRepoEntity: GithubRepoEntity) = launch {
        withContext(Dispatchers.IO) {
            val isLike = repositoryDao.getRepository(githubRepoEntity.fullName) != null
            withContext(Dispatchers.Main) {
                setLikeImage(isLike)
                binding.likeButton.setOnClickListener {
                    likeRepository(githubRepoEntity, isLike)
                }
            }
        }
    }

    private fun likeRepository(githubRepoEntity: GithubRepoEntity, isLike: Boolean) = launch {
        withContext(Dispatchers.IO) {
            if (isLike) {
                repositoryDao.remove(githubRepoEntity.fullName)
            } else {
                repositoryDao.insert(githubRepoEntity)
            }

            withContext(Dispatchers.Main) {
                setLikeImage(!isLike)
            }
        }
    }

    private fun setLikeImage(isLike: Boolean) {
        binding.likeButton.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                if (isLike) {
                    R.drawable.baseline_favorite_24
                } else {
                    R.drawable.baseline_favorite_border_24
                }
            )
        )
    }

    private fun showProgress(isShown: Boolean) = with(binding) {
        progressBar.isGone = !isShown
    }

    private fun Context.toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}