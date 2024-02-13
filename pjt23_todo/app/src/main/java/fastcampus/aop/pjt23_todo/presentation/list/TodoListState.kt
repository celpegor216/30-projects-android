package fastcampus.aop.pjt23_todo.presentation.list

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity

sealed class TodoListState {
    object UnInitialized: TodoListState()

    object Loading: TodoListState()

    data class Success(
        val todoList: List<TodoEntity>
    ): TodoListState()

    object Error: TodoListState()
}