package fastcampus.aop.pjt04_calculator.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fastcampus.aop.pjt04_calculator.model.History

// Entity 조회/추가/수정/삭제와 관련된 함수 정의
@Dao
interface HistoryDao {

    // 전체 조회
    @Query("SELECT * FROM history")
    fun getAll(): List<History>

    // 추가
    @Insert
    fun insertHistory(history: History)

    // 전체 삭제
    @Query("DELETE FROM history")
    fun deleteAll()

    // 개별 삭제
    @Delete
    fun delete(history: History)

    // 인자로 받은 result 문자열을 result 컬럼 값에서 포함하는 항목들 조회
    @Query("SELECT * FROM history WHERE result LIKE :result")
    fun findByResult(result: String): List<History>
}