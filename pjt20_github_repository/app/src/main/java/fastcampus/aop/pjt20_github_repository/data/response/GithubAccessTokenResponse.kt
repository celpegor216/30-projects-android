package fastcampus.aop.pjt20_github_repository.data.response

data class GithubAccessTokenResponse(
    val accessToken: String,
    val scope: String,
    val tokenType: String
)