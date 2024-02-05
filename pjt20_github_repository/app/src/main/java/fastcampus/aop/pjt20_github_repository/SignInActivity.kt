package fastcampus.aop.pjt20_github_repository

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isGone
import fastcampus.aop.pjt20_github_repository.databinding.ActivitySignInBinding
import fastcampus.aop.pjt20_github_repository.utility.AuthTokenProvider
import fastcampus.aop.pjt20_github_repository.utility.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class SignInActivity : AppCompatActivity(), CoroutineScope {

    // launch()를 호출할 때 사용할 scope 정의
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private lateinit var binding: ActivitySignInBinding

    private val authTokenProvider by lazy {
        AuthTokenProvider(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkAuthCodeExist()) {
            launchMainActivity()
        } else {
            initViews()
        }
    }

    private fun checkAuthCodeExist(): Boolean = !authTokenProvider.token.isNullOrEmpty()

    private fun launchMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun initViews() = with(binding) {
        loginButton.setOnClickListener {
            // github 로그인 후 인가 코드 요청 url은 https://docs.github.com/ko/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#1-request-a-users-github-identity 에서 확인할 수 있음
            val loginUrl = Uri.Builder().scheme("https").authority("github.com")
                .appendPath("login")
                .appendPath("oauth")
                .appendPath("authorize")
                .appendQueryParameter("client_id", BuildConfig.GITHUB_CLIENT_ID)
                .build()

            // 다른 브라우저 앱을 표시하지 않고 앱 내에서 브라우저 표시
            CustomTabsIntent.Builder().build().also { customTabsIntent ->
                customTabsIntent.launchUrl(this@SignInActivity, loginUrl)
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        // github 로그인에 성공했다면 받아온 인가 코드로 access token을 요청
        intent?.data?.getQueryParameter("code")?.let { code ->
            launch(coroutineContext) {
                showProgress()
                val getAccessTokenJob = getAccessToken(code)
                getAccessTokenJob.join()
                dismissProgress()
                if (checkAuthCodeExist()) {
                    launchMainActivity()
                }
            }
        }
    }

    private fun showProgress() = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            with(binding) {
                loginButton.isGone = true
                progressBar.isGone = false
                progressTextView.isGone = false
            }
        }
    }

    private fun getAccessToken(code: String) = launch(coroutineContext) {
        try {
            withContext(Dispatchers.IO) {
                val response = RetrofitUtil.authApiService.getAccessToken(
                    clientId = BuildConfig.GITHUB_CLIENT_ID,
                    clientSecret = BuildConfig.GITHUB_CLIENT_SECRET,
                    code = code
                )

                val accessToken = response.accessToken

                if (accessToken.isNotEmpty()) {
                    withContext(coroutineContext) {
                        authTokenProvider.updateToken(accessToken)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this@SignInActivity, "로그인 과정에서 에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun dismissProgress() = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            with(binding) {
                loginButton.isGone = false
                progressBar.isGone = true
                progressTextView.isGone = true
            }
        }
    }
}