package fastcampus.aop.pjt23_todo.di

import android.content.Context
import androidx.room.Room
import fastcampus.aop.pjt23_todo.data.local.db.TodoDatabase
import fastcampus.aop.pjt23_todo.data.repository.DefaultTodoRepository
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
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val appModule = module {
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
    single<TodoRepository> { DefaultTodoRepository(get(), get()) }
    single { provideDB(androidApplication()) }
    single { provideTodoDao(get()) }

    // Coroutine Dispatchers
    single { Dispatchers.Main }
    single { Dispatchers.IO }
}

internal fun provideDB(context: Context): TodoDatabase =
    Room.databaseBuilder(context, TodoDatabase::class.java, TodoDatabase.DB_NAME)
        .build()

internal fun provideTodoDao(database: TodoDatabase) = database.todoDao()