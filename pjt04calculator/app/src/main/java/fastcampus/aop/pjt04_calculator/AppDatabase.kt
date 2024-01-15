package fastcampus.aop.pjt04_calculator

import androidx.room.Database
import androidx.room.RoomDatabase
import fastcampus.aop.pjt04_calculator.dao.HistoryDao
import fastcampus.aop.pjt04_calculator.model.History

// 앱이 업데이트되면서 DB 구조가 변경될 수 있는데,
// 이를 위해 version 코드를 지정해서 특정 version에서 다른 version으로 이전하는 작업을 수행해야 함
@Database(entities = [History::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}