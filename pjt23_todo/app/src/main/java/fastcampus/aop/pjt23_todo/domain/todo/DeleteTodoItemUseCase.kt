package fastcampus.aop.pjt23_todo.domain.todo

import fastcampus.aop.pjt23_todo.data.repository.TodoRepository
import fastcampus.aop.pjt23_todo.domain.UseCase

class DeleteTodoItemUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke(itemId: Long) {
        return todoRepository.deleteTodoItem(itemId)
    }
}