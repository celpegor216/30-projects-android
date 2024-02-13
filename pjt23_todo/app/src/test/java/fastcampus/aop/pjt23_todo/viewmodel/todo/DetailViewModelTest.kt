package fastcampus.aop.pjt23_todo.viewmodel.todo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fastcampus.aop.pjt23_todo.ViewModelTest
import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.domain.todo.InsertTodoItemUseCase
import fastcampus.aop.pjt23_todo.presentation.detail.DetailMode
import fastcampus.aop.pjt23_todo.presentation.detail.DetailViewModel
import fastcampus.aop.pjt23_todo.presentation.detail.TodoDetailState
import fastcampus.aop.pjt23_todo.presentation.list.ListViewModel
import fastcampus.aop.pjt23_todo.presentation.list.TodoListState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test Item Delete
 * 4. test Item Update
 */
internal class DetailViewModelTest: ViewModelTest() {

    private val id = 1L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.DETAIL, id) }
    private val listViewModel by inject<ListViewModel>()

    private val insertTodoItemUseCase: InsertTodoItemUseCase by inject()

    private val todo = TodoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Before
    fun init() {
        initData()
    }

    // 1. initData()
    private fun initData() = runBlockingTest {
        insertTodoItemUseCase(todo)
    }

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // 2. test viewModel fetch
    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()

        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Success(todo)
            )
        )
    }

    // 3. test Item Delete
    @Test
    fun `test Item Delete`() = runBlockingTest {
        val detailTestObservable = detailViewModel.todoDetailLiveData.test()

        detailViewModel.deleteItem()

        detailTestObservable.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Delete
            )
        )

        val listTestObservable = listViewModel.todoListLiveData.test()
        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                TodoListState.UnInitialized,
                TodoListState.Loading,
                TodoListState.Success(listOf())
            )
        )
    }

    // 4. test Item Update
    @Test
    fun `test Item Update`() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()

        val updateTitle = "title 1 update"
        val updateDescription = "description 1 update"
        val updateToDo = todo.copy(
            title = updateTitle,
            description = updateDescription
        )

        detailViewModel.writeItem(
            title = updateTitle,
            description = updateDescription
        )

        testObservable.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Success(updateToDo)
            )
        )
    }
}