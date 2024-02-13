package fastcampus.aop.pjt23_todo.presentation.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isGone
import fastcampus.aop.pjt23_todo.R
import fastcampus.aop.pjt23_todo.databinding.ActivityDetailBinding
import fastcampus.aop.pjt23_todo.presentation.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import kotlin.coroutines.CoroutineContext

internal class DetailActivity : BaseActivity<DetailViewModel>(), CoroutineScope {
    companion object {
        const val TODO_ID_KEY = "TodoId"
        const val DETAIL_MODE_KEY = "DetailMode"

        const val FETCH_REQUEST_CODE = 10

        // 생성
        fun getIntent(context: Context, detailMode: DetailMode) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DETAIL_MODE_KEY, detailMode)
            }

        // 상세
        fun getIntent(context: Context, id: Long, detailMode: DetailMode) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(DETAIL_MODE_KEY, detailMode)
                putExtra(TODO_ID_KEY, id)
            }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override val viewModel: DetailViewModel by viewModel {
        parametersOf(
            intent.getSerializableExtra(DETAIL_MODE_KEY),
            intent.getLongExtra(TODO_ID_KEY, -1)
        )
    }

    private lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setResult(Activity.RESULT_OK)
    }

    override fun observeData() = viewModel.todoDetailLiveData.observe(this) {
        when (it) {
            is TodoDetailState.UnInitialized -> {
                initViews()
            }
            is TodoDetailState.Loading -> {
                handleLoadingState()
            }
            is TodoDetailState.Success -> {
                handleSuccessState(it)
            }
            is TodoDetailState.Write -> {
                handleWriteState()
            }
            is TodoDetailState.Modify -> {
                handleModifyState()
            }
            is TodoDetailState.Delete -> {
                Toast.makeText(this, "성공적으로 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
            is TodoDetailState.Error -> {
                Toast.makeText(this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun initViews() = with(binding) {
        titleEditText.isEnabled = false
        descriptionEditText.isEnabled = false

        deleteButton.isGone = true
        updateButton.isGone = true
        saveButton.isGone = true

        deleteButton.setOnClickListener {
            viewModel.deleteItem()
        }
        updateButton.setOnClickListener {
            viewModel.setModifyMode()
        }
        saveButton.setOnClickListener {
            viewModel.writeItem(
                title = titleEditText.text.toString(),
                description = descriptionEditText.text.toString()
            )
        }
    }

    private fun handleLoadingState() = with(binding) {
        progressBar.isGone = false
    }

    private fun handleSuccessState(state: TodoDetailState.Success) = with(binding) {
        progressBar.isGone = true

        titleEditText.isEnabled = false
        descriptionEditText.isEnabled = false

        deleteButton.isGone = false
        updateButton.isGone = false
        saveButton.isGone = true

        val todoItem = state.todoItem
        titleEditText.setText(todoItem.title)
        descriptionEditText.setText(todoItem.description)
    }

    private fun handleWriteState() = with(binding) {
        titleEditText.isEnabled = true
        descriptionEditText.isEnabled = true

        saveButton.isGone = false
    }

    private fun handleModifyState() = with(binding) {
        titleEditText.isEnabled = true
        descriptionEditText.isEnabled = true

        deleteButton.isGone = true
        updateButton.isGone = true
        saveButton.isGone = false
    }
}