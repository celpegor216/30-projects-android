package fastcampus.aop.pjt18_map.utility

import fastcampus.aop.pjt18_map.Key
import fastcampus.aop.pjt18_map.Url
import fastcampus.aop.pjt18_map.response.address.AddressInfoResponse
import fastcampus.aop.pjt18_map.response.search.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {

    @GET(Url.GET_TMAP_LOCATION)
    suspend fun getSearchLocation(
        @Header("appkey") appKey: String = Key.TMAP_API_KEY,
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("count") count: Int = 20,
        @Query("searchKeyword") keyword: String,
        @Query("areaLLCode") areaLLCode: String? = null,
        @Query("areaLMCode") areaLMCode: String? = null,
        @Query("resCoordType") resCoordType: String? = null,
        @Query("searchType") searchType: String? = null,
        @Query("multiPoint") multiPoint: String? = null,
        @Query("searchtypCd") searchtypCd: String? = null,
        @Query("radius") radius: String? = null,
        @Query("reqCoordType") reqCoordType: String? = null,
        @Query("centerLat") centerLat: String? = null,
        @Query("centerLon") centerLon: String? = null
        ): Response<SearchResponse>

    @GET(Url.GET_TMAP_REVERSE_GOI_CODE)
    suspend fun getReverseGeoCode(
        @Header("appkey") appKey: String = Key.TMAP_API_KEY,
        @Query("version") version: Int = 1,
        @Query("callback") callback: String? = null,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("coordType") coordType: String? = null,
        @Query("addressType") addressType: String? = null,
    ): Response<AddressInfoResponse>
}