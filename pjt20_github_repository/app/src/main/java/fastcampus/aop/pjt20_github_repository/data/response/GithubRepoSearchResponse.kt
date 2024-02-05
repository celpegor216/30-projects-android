package fastcampus.aop.pjt20_github_repository.data.response

import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity

data class GithubRepoSearchResponse(
    val totalCount: Int,
    val items: List<GithubRepoEntity>
)
