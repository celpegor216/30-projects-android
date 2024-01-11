package fastcampus.aop.pjt03_secret_diary

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener

class DiaryActivity: AppCompatActivity() {
    private val diaryEditText by lazy {
        findViewById<EditText>(R.id.diaryEditText)
    }

    // Main Looper에 연결된 handler 생성
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diaryPreference = getSharedPreferences("diary", Context.MODE_PRIVATE)

        diaryEditText.setText(diaryPreference.getString("diary", ""))

        // 비동기 작업 처리를 위한 runnable 생성
        val runnable = Runnable {
            getSharedPreferences("diary", Context.MODE_PRIVATE).edit {
                putString("diary", diaryEditText.text.toString())
            }
        }

        diaryEditText.addTextChangedListener {
            // 아직 실행되지 않은 runnable이 있을 경우 삭제
            handler.removeCallbacks(runnable)

            // UI 스레드와 추가로 생성한 스레드를 연결하기 위해 handler 사용
            // 내용이 작성되고 0.5초 이후에 작업이 실행됨
            handler.postDelayed(runnable, 500)
        }
    }
}