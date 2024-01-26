package fastcampus.aop.pjt18_map

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import fastcampus.aop.pjt18_map.MapActivity.Companion.SEARCH_RESULT_EXTRA_KEY
import fastcampus.aop.pjt18_map.databinding.ActivityMainBinding
import fastcampus.aop.pjt18_map.model.LocationLatLngEntity
import fastcampus.aop.pjt18_map.model.SearchResultEntity
import fastcampus.aop.pjt18_map.response.search.Poi
import fastcampus.aop.pjt18_map.response.search.Pois
import fastcampus.aop.pjt18_map.utility.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job

    // CoroutineScope 인터페이스 상속 시 오버라이딩 필요
    // 어떤 스레드에서 동작할 것인지 명시
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityMainBinding
    private lateinit var searchRecyclerAdapter: SearchRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initAdapter()
        initViews()
        bindViews()
        initData()
    }

    private fun initAdapter() {
        searchRecyclerAdapter = SearchRecyclerAdapter()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = true
        recyclerView.adapter = searchRecyclerAdapter
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            val keyword = searchEditText.text.toString()
            searchKeyword(keyword)
        }
    }

    private fun searchKeyword(keyword: String) {
        // Main 스레드에서 비동기 작업 시작
        launch(coroutineContext) {
            try {
                // IO 스레드로 변경해서 데이터를 가져오는 네트워크 작업 수행
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(keyword = keyword)

                    if (response.isSuccessful) {
                        // Main 스레드로 변경해서 가져온 데이터를 화면에 표시하는 작업 수행
                        withContext(Dispatchers.Main) {
                            response.body()?.let { searchResponse ->
                                setData(searchResponse.searchPoiInfo.pois)
                            }

                            binding.emptyResultTextView.isVisible = false
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun initData() {
        searchRecyclerAdapter.notifyDataSetChanged()
    }

    private fun setData(pois: Pois) {
        val data = pois.poi.map {
            SearchResultEntity(
                name = it.name ?: "빌딩명 없음",
                fullAddress = makeMainAddress(it),
                locationLatLngEntity = LocationLatLngEntity(
                    it.noorLat,
                    it.noorLon
                )
            )
        }
        searchRecyclerAdapter.setSearchResultList(data) {
            startActivity(Intent(this, MapActivity::class.java).apply {
                // Parcelable 형태로 전달되어 MapActivity에서 쉽게 사용할 수 있음
                putExtra(SEARCH_RESULT_EXTRA_KEY, it)
            })
        }
    }

    private fun makeMainAddress(poi: Poi): String =
        if (poi.secondNo?.trim().isNullOrEmpty()) {
            (poi.upperAddrName?.trim() ?: "") + " " +
                (poi.middleAddrName?.trim() ?: "") + " " +
                (poi.lowerAddrName?.trim() ?: "") + " " +
                (poi.detailAddrName?.trim() ?: "") + " " +
                poi.firstNo?.trim()
        } else {
            (poi.upperAddrName?.trim() ?: "") + " " +
                (poi.middleAddrName?.trim() ?: "") + " " +
                (poi.lowerAddrName?.trim() ?: "") + " " +
                (poi.detailAddrName?.trim() ?: "") + " " +
                (poi.firstNo?.trim() ?: "") + " " +
                poi.secondNo?.trim()
        }
}