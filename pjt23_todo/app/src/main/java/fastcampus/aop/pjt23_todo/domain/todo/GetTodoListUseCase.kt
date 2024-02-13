package fastcampus.aop.pjt23_todo.domain.todo

import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.data.repository.TodoRepository
import fastcampus.aop.pjt23_todo.domain.UseCase

class GetTodoListUseCase(
    private val todoRepository: TodoRepository
): UseCase {

    suspend operator fun invoke(): List<TodoEntity> {
        return todoRepository.getTodoList()
    }
}