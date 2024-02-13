package fastcampus.aop.pjt23_todo.viewmodel.todo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fastcampus.aop.pjt23_todo.ViewModelTest
import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.presentation.detail.DetailMode
import fastcampus.aop.pjt23_todo.presentation.detail.DetailViewModel
import fastcampus.aop.pjt23_todo.presentation.detail.TodoDetailState
import fastcampus.aop.pjt23_todo.presentation.list.ListViewModel
import fastcampus.aop.pjt23_todo.presentation.list.TodoListState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.test.inject

/**
 * [DetailViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. test viewModel fetch
 * 2. test Item Insert
 */
internal class DetailViewModelForWriteTest : ViewModelTest() {

    private val id = 0L

    private val detailViewModel by inject<DetailViewModel> { parametersOf(DetailMode.WRITE, id) }
    private val listViewModel by inject<ListViewModel>()

    private val todo = TodoEntity(
        id = id,
        title = "title $id",
        description = "description $id",
        hasCompleted = false
    )

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // 1. test viewModel fetch
    @Test
    fun `test viewModel fetch`() = runBlockingTest {
        val testObservable = detailViewModel.todoDetailLiveData.test()

        detailViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Write
            )
        )
    }

    // 2. test Item Insert
    @Test
    fun `test Item Update`() = runBlockingTest {
        val detailTestObservable = detailViewModel.todoDetailLiveData.test()

        detailViewModel.writeItem(
            title = todo.title,
            description = todo.description
        )

        detailTestObservable.assertValueSequence(
            listOf(
                TodoDetailState.UnInitialized,
                TodoDetailState.Loading,
                TodoDetailState.Success(todo)
            )
        )

        assert(detailViewModel.detailMode == DetailMode.DETAIL)
        assert(detailViewModel.id == id)

        val listTestObservable = listViewModel.todoListLiveData.test()

        listViewModel.fetchData()
        listTestObservable.assertValueSequence(
            listOf(
                TodoListState.UnInitialized,
                TodoListState.Loading,
                TodoListState.Success(listOf(todo))
            )
        )
    }
}