package fastcampus.aop.pjt20_github_repository.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.pjt20_github_repository.data.dao.RepositoryDao
import fastcampus.aop.pjt20_github_repository.data.entity.GithubRepoEntity

@Database(entities = [GithubRepoEntity::class], version = 1)
abstract class SimpleGithubDatabase: RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}