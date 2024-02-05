package fastcampus.aop.pjt20_github_repository.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity

@Dao
interface RepositoryDao {
    @Insert
    suspend fun insert(repo: GithubRepoEntity)

    @Query("SELECT * FROM githubrepository")
    suspend fun getAllRepository(): List<GithubRepoEntity>

    @Query("SELECT * FROM githubrepository WHERE fullName = :fullName")
    suspend fun getRepository(fullName: String): GithubRepoEntity?

    @Query("DELETE FROM githubrepository WHERE fullName = :fullName")
    suspend fun remove(fullName: String)
}