package fastcampus.aop.pjt12_book_review

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import fastcampus.aop.pjt12_book_review.dao.HistoryDao
import fastcampus.aop.pjt12_book_review.dao.ReviewDao
import fastcampus.aop.pjt12_book_review.model.History
import fastcampus.aop.pjt12_book_review.model.Review

@Database(entities = [History::class, Review::class], version = 2)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun reviewDao(): ReviewDao
}

fun getAppDatabase(context: Context): AppDatabase {

    // DB가 변경되었을 때 앱을 삭제 후 재설치하지 않고, 마이그레이션을 통해 변경하는 방법
    val migration_1_to_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE REVIEW RENAME COLUMN id TO isbn")
        }
    }

    return Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "BookSearchDB"
    )
        .addMigrations(migration_1_to_2)
        .build()
}