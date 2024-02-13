package fastcampus.aop.pjt23_todo.data

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.data.repository.TodoRepository

class TestTodoRepository: TodoRepository {

    private val todoList: MutableList<TodoEntity> = mutableListOf()

    override suspend fun getTodoItem(itemId: Long): TodoEntity? {
        return todoList.find { it.id == itemId }
    }

    override suspend fun getTodoList(): List<TodoEntity> {
        return todoList
    }

    override suspend fun insertTodoItem(todoEntity: TodoEntity): Long {
        todoList.add(todoEntity)
        return todoEntity.id
    }

    override suspend fun insertTodoList(todoList: List<TodoEntity>) {
        this.todoList.addAll(todoList)
    }

    override suspend fun updateTodoItem(todoItem: TodoEntity): Boolean {
        val foundTodoEntity = todoList.find { it.id == todoItem.id }
        return if (foundTodoEntity == null) {
            false
        } else {
            this.todoList[todoList.indexOf(foundTodoEntity)] = todoItem
            true
        }
    }

    override suspend fun deleteTodoItem(itemId: Long): Boolean {
        val foundTodoEntity = todoList.find { it.id == itemId }
        return if (foundTodoEntity == null) {
            false
        } else {
            this.todoList.removeAt(todoList.indexOf(foundTodoEntity))
            true
        }
    }

    override suspend fun deleteAll() {
        todoList.clear()
    }
}