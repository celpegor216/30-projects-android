package fastcampus.aop.pjt23_todo.domain.todo

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.data.repository.TodoRepository
import fastcampus.aop.pjt23_todo.domain.UseCase

class DeleteAllTodoItemUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke() {
        return todoRepository.deleteAll()
    }
}