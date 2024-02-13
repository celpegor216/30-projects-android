package fastcampus.aop.pjt23_todo.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.domain.todo.DeleteAllTodoItemUseCase
import fastcampus.aop.pjt23_todo.domain.todo.GetTodoListUseCase
import fastcampus.aop.pjt23_todo.domain.todo.UpdateTodoUseCase
import fastcampus.aop.pjt23_todo.presentation.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 필요한 UseCase
 *
 * 1. [GetTodoListUseCase]
 * 2. [UpdateTodoUseCase]
 * 3. [DeleteAllTodoItemUseCase]
 * */

internal class ListViewModel(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase,
    private val deleteAllTodoItemUseCase: DeleteAllTodoItemUseCase
): BaseViewModel() {

    private var _todoListLiveData = MutableLiveData<TodoListState>(TodoListState.UnInitialized)
    val todoListLiveData: LiveData<TodoListState> = _todoListLiveData

    override fun fetchData(): Job= viewModelScope.launch {
        _todoListLiveData.postValue(TodoListState.Loading)
        _todoListLiveData.postValue(TodoListState.Success(getTodoListUseCase()))
    }

    fun updateEntity(todoEntity: TodoEntity) = viewModelScope.launch {
        updateTodoUseCase(todoEntity)
    }

    fun deleteAll() = viewModelScope.launch {
        _todoListLiveData.postValue(TodoListState.Loading)
        deleteAllTodoItemUseCase()
        _todoListLiveData.postValue(TodoListState.Success(getTodoListUseCase()))
    }
}