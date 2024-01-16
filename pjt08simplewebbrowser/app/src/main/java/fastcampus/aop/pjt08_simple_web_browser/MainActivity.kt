package fastcampus.aop.pjt08_simple_web_browser

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.ContentLoadingProgressBar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class MainActivity : AppCompatActivity() {

    private val progressBar: ContentLoadingProgressBar by lazy {
        findViewById(R.id.progressBar)
    }
    private val refreshLayout: SwipeRefreshLayout by lazy {
        findViewById(R.id.refreshLayout)
    }
    private val webView: WebView by lazy {
        findViewById(R.id.webView)
    }
    private val addressBar: EditText by lazy {
        findViewById(R.id.addressBar)
    }
    private val goHomeButton: ImageButton by lazy {
        findViewById(R.id.goHomeButton)
    }
    private val goBackButton: ImageButton by lazy {
        findViewById(R.id.goBackButton)
    }
    private val goForwardButton: ImageButton by lazy {
        findViewById(R.id.goForwardButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        bindViews()
    }

    // 기기의 뒤로가기 버튼을 눌렀을 때 동작 지정
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun initViews() {
        webView.apply {
            // webViewClient를 지정하지 않으면 기기의 기본 브라우저로 이동함
            webViewClient = WebViewClient()

            // 안드로이드에서는 보안 상의 이슈로 자바 스크립트를 기본적으로 허용해두지 않음
            settings.javaScriptEnabled = true

            // 안드로이드 버전 9부터 http로 입력할 경우
            // CLEARTEXT_NOT_PERMITTED 에러 발생 -> 암호화된 주소(https)를 사용해야 함
            // http도 지원할 경우 AndroidManifest 파일의 application에
            // useClearTextTraffic 속성을 true로 지정
            loadUrl(DEFAULT_URL)

            webChromeClient = WebChromeClient()
        }
    }

    private fun bindViews() {
        addressBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                var loadingUrl = textView.text.toString()

                if (!URLUtil.isNetworkUrl(loadingUrl)) {
                    loadingUrl = "http://$loadingUrl"
                }
                webView.loadUrl(loadingUrl)
            }

            // true를 반환할 경우, 해당 액션의 결과를 다른 곳에서도 사용하겠다는 의미로
            // 키보드가 닫히지 않기 때문에 false를 반환
            return@setOnEditorActionListener false
        }

        goBackButton.setOnClickListener {
            webView.goBack()
        }

        goForwardButton.setOnClickListener {
            webView.goForward()
        }

        goHomeButton.setOnClickListener {
            webView.loadUrl(DEFAULT_URL)
        }

        refreshLayout.setOnRefreshListener {
            webView.reload()
        }
    }

    // inner class: 상위 클래스에 접근 가능
    inner class WebViewClient: android.webkit.WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

            progressBar.show()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

            refreshLayout.isRefreshing = false
            progressBar.hide()

            goBackButton.isEnabled = webView.canGoBack()
            goForwardButton.isEnabled = webView.canGoForward()

            addressBar.setText(url)
        }
    }

    // WebViewClient 보다 브라우저 관점에서의 기능을 제공
    inner class WebChromeClient: android.webkit.WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)

            progressBar.progress = newProgress
        }
    }

    companion object {
        private const val DEFAULT_URL = "https://www.google.com"
    }
}