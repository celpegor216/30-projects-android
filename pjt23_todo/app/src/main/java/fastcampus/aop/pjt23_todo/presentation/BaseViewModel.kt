package fastcampus.aop.pjt23_todo.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

internal abstract class BaseViewModel: ViewModel() {
    abstract fun fetchData(): Job
}