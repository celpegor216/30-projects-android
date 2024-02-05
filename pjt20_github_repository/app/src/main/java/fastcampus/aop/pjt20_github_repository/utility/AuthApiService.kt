package fastcampus.aop.pjt20_github_repository.utility

import fastcampus.aop.pjt20_github_repository.data.response.GithubAccessTokenResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {
    // github 엑세스 토큰 요청 url은 https://docs.github.com/ko/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps#2-users-are-redirected-back-to-your-site-by-github 에서 확인할 수 있음
    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    suspend fun getAccessToken (
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("code") code: String
    ): GithubAccessTokenResponse
}