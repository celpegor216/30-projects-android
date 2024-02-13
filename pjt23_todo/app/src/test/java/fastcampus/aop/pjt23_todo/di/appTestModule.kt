package fastcampus.aop.pjt23_todo.di

import fastcampus.aop.pjt23_todo.data.TestTodoRepository
import fastcampus.aop.pjt23_todo.data.repository.TodoRepository
import fastcampus.aop.pjt23_todo.domain.todo.DeleteAllTodoItemUseCase
import fastcampus.aop.pjt23_todo.domain.todo.DeleteTodoItemUseCase
import fastcampus.aop.pjt23_todo.domain.todo.GetTodoItemUseCase
import fastcampus.aop.pjt23_todo.domain.todo.GetTodoListUseCase
import fastcampus.aop.pjt23_todo.domain.todo.InsertTodoItemUseCase
import fastcampus.aop.pjt23_todo.domain.todo.InsertTodoListUseCase
import fastcampus.aop.pjt23_todo.domain.todo.UpdateTodoUseCase
import fastcampus.aop.pjt23_todo.presentation.detail.DetailMode
import fastcampus.aop.pjt23_todo.presentation.detail.DetailViewModel
import fastcampus.aop.pjt23_todo.presentation.list.ListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appTestModule = module {

    // ViewModel
    viewModel { ListViewModel(get(), get(), get()) }
    viewModel { (detailMode: DetailMode, id: Long) ->
        DetailViewModel(
            detailMode = detailMode,
            id = id,
            get(),
            get(),
            get(),
            get()
        )
    }

    // UseCase
    factory { GetTodoItemUseCase(get()) }
    factory { GetTodoListUseCase(get()) }
    factory { InsertTodoItemUseCase(get()) }
    factory { InsertTodoListUseCase(get()) }
    factory { UpdateTodoUseCase(get()) }
    factory { DeleteTodoItemUseCase(get()) }
    factory { DeleteAllTodoItemUseCase(get()) }

    // Repository
    single<TodoRepository> { TestTodoRepository() }

}