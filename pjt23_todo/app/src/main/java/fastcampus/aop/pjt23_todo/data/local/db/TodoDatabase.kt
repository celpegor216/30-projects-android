package fastcampus.aop.pjt23_todo.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.data.local.db.dao.TodoDao

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class TodoDatabase: RoomDatabase() {
    companion object {
        const val DB_NAME = "TodoDatabase.db"
    }

    abstract fun todoDao(): TodoDao
}