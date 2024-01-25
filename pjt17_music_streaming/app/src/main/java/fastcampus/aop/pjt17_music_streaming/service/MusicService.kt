package fastcampus.aop.pjt17_music_streaming.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {

    @GET("/v3/3b920bc6-547e-4f5c-a13a-8519dcb194ad")
    fun listMusics(): Call<MusicDto>
}