package fastcampus.aop.pjt23_todo.domain.todo

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.data.repository.TodoRepository
import fastcampus.aop.pjt23_todo.domain.UseCase

class GetTodoItemUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke(itemId: Long): TodoEntity? {
        return todoRepository.getTodoItem(itemId)
    }
}