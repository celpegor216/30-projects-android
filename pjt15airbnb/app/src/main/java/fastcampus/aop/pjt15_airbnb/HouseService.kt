package fastcampus.aop.pjt15_airbnb

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/d44e4993-e0c5-419a-b352-79e6063e42b2")
    fun getHouseList(): Call<HouseDto>
}