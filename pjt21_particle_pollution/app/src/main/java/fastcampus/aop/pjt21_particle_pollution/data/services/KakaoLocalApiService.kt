package fastcampus.aop.pjt21_particle_pollution.data.services

import fastcampus.aop.pjt21_particle_pollution.BuildConfig
import fastcampus.aop.pjt21_particle_pollution.data.models.tmcoordinates.TmCoordinatesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoLocalApiService {

    @GET("/v2/local/geo/transcoord.json?output_coord=TM")
    @Headers("Authorization: KakaoAK ${BuildConfig.KAKAO_API_KEY}")
    suspend fun getTmCoordinates(
        @Query("x") longitude: Double,
        @Query("y") latitude: Double
    ): Response<TmCoordinatesResponse>
}