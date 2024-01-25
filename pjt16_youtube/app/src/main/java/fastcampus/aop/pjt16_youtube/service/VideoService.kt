package fastcampus.aop.pjt16_youtube.service

import fastcampus.aop.pjt16_youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/c2aa855a-bc52-49aa-b9de-0a5dd2786e14")
    fun listVideos(): Call<VideoDto>
}