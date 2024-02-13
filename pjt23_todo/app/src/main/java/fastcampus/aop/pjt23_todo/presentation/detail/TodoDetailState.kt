package fastcampus.aop.pjt23_todo.presentation.detail

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity

sealed class TodoDetailState {
    object UnInitialized: TodoDetailState()

    object Loading: TodoDetailState()

    data class Success(
        val todoItem: TodoEntity
    ): TodoDetailState()

    object Error: TodoDetailState()

    object Modify: TodoDetailState()

    object Delete: TodoDetailState()

    object Write: TodoDetailState()
}