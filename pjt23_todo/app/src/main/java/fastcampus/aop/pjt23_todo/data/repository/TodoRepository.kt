package fastcampus.aop.pjt23_todo.data.repository

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity

interface TodoRepository {

    suspend fun getTodoItem(itemId: Long): TodoEntity?

    suspend fun getTodoList(): List<TodoEntity>

    suspend fun insertTodoItem(todoEntity: TodoEntity): Long

    suspend fun insertTodoList(todoList: List<TodoEntity>)

    suspend fun updateTodoItem(todoEntity: TodoEntity)

    suspend fun deleteTodoItem(itemId: Long)

    suspend fun deleteAll()
}