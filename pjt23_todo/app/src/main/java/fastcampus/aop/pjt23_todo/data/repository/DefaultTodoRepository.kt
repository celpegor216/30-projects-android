package fastcampus.aop.pjt23_todo.data.repository

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.data.local.db.dao.TodoDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultTodoRepository(
    private val todoDao: TodoDao,
    private val ioDispatcher: CoroutineDispatcher
): TodoRepository {

    override suspend fun getTodoItem(itemId: Long): TodoEntity? = withContext(ioDispatcher) {
        todoDao.getById(itemId)
    }
    override suspend fun getTodoList(): List<TodoEntity> = withContext(ioDispatcher) {
        todoDao.getAll()
    }

    override suspend fun insertTodoItem(todoEntity: TodoEntity): Long = withContext(ioDispatcher) {
        todoDao.insert(todoEntity)
    }

    override suspend fun insertTodoList(todoList: List<TodoEntity>) = withContext(ioDispatcher) {
        todoDao.insert(todoList)
    }

    override suspend fun updateTodoItem(todoEntity: TodoEntity) = withContext(ioDispatcher) {
        todoDao.update(todoEntity)
    }

    override suspend fun deleteTodoItem(itemId: Long) = withContext(ioDispatcher) {
        todoDao.delete(itemId)
    }

    override suspend fun deleteAll() = withContext(ioDispatcher) {
        todoDao.deleteAll()
    }
}