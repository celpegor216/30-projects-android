package fastcampus.aop.pjt20_github_repository.data.database

import android.content.Context
import androidx.room.Room

object DataBaseProvider {
    private const val DB_NAME = "github_repository_app.db"

    fun provideDB(applicationContext: Context) = Room.databaseBuilder(
        applicationContext,
        SimpleGithubDatabase::class.java,
        DB_NAME
    ).build()
}