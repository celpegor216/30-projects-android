package fastcampus.aop.pjt12_book_review

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fastcampus.aop.pjt12_book_review.adapter.BookAdapter
import fastcampus.aop.pjt12_book_review.adapter.HistoryAdapter
import fastcampus.aop.pjt12_book_review.api.BookService
import fastcampus.aop.pjt12_book_review.databinding.ActivityMainBinding
import fastcampus.aop.pjt12_book_review.model.History
import fastcampus.aop.pjt12_book_review.model.SearchBooksDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BookAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var bookService: BookService
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        db = getAppDatabase(this)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        bookService = retrofit.create(BookService::class.java)

        // 베스트셀러 API가 없어 임의로 초기 데이터를 표시하기 위해 검색 수행
        search("안드로이드")
    }

    private fun initBookRecyclerView() {
        adapter = BookAdapter {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra("bookModel", it)
            startActivity(intent)
        }
        binding.bookRecyclerView.adapter = adapter
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter {
            deleteSearchKeyword(it)
        }
        binding.historyRecyclerView.adapter = historyAdapter
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initSearchEditText() {
        binding.searchEditText.setOnKeyListener { view, keyCode, keyEvent ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString())

                // 이벤트가 처리되었음을 의미
                return@setOnKeyListener true
            }

            // 이벤트가 처리되지 않아 시스템에서 정의한 다른 이벤트가 실행되어야 함을 의미
            return@setOnKeyListener false
        }
        
        binding.searchEditText.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun search(keyword: String) {
        bookService.getBooksByName(getString(R.string.naverAPIKey), getString(R.string.naverSecretKey), keyword)
            .enqueue(object : Callback<SearchBooksDto> {
                override fun onResponse(
                    call: Call<SearchBooksDto>,
                    response: Response<SearchBooksDto>
                ) {
                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if (!response.isSuccessful) {
                        Log.e(TAG, "책 목록을 불러오는 데 실패하였습니다.")
                        return
                    }

                    adapter.submitList(response.body()?.books.orEmpty())
                }

                override fun onFailure(call: Call<SearchBooksDto>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG, "책 목록을 불러오는 데 실패하였습니다.")
                }

            })
    }

    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed()

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread{
            db.historyDao().deleteHistory(keyword)
            showHistoryView()
        }.start()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}