package fastcampus.aop.pjt23_todo.viewmodel.todo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fastcampus.aop.pjt23_todo.ViewModelTest
import fastcampus.aop.pjt23_todo.data.entity.TodoEntity
import fastcampus.aop.pjt23_todo.domain.todo.GetTodoItemUseCase
import fastcampus.aop.pjt23_todo.domain.todo.InsertTodoListUseCase
import fastcampus.aop.pjt23_todo.presentation.list.ListViewModel
import fastcampus.aop.pjt23_todo.presentation.list.TodoListState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.inject
import org.mockito.kotlin.description

/**
 * [ListViewModel]을 테스트하기 위한 Unit Test Class
 *
 * 1. initData()
 * 2. test viewModel fetch
 * 3. test Item Update
 * 4. test Item Delete All
 */

internal class ListViewModelTest: ViewModelTest() {

    private val viewModel: ListViewModel by inject()

    private val insertTodoListUseCase: InsertTodoListUseCase by inject()
    private val getTodoItemUseCase: GetTodoItemUseCase by inject()

    private val mockList = (0 until 10).map {
        TodoEntity(
            id = it.toLong(),
            title = "title $it",
            description = "description $it",
            hasCompleted = false
        )
    }

    /**
     * 필요한 UseCase
     *
     * 1. InsertTodoListUseCase
     * 2. GetTodoItemUseCase
     * */

    @Before
    fun init() {
        initData()
    }

    // 1. initData()
    private fun initData() = runBlockingTest {
        insertTodoListUseCase(mockList)
    }

    // Method getMainLooper in android.os.Looper not mocked 에러 발생
    // LiveData를 사용하는 ViewModel 테스트를 진행할 때 생기는 문제
    // test 폴더에서의 테스트(로컬 테스트)는 디바이스를 이용한 테스트가 아님
    // JVM에서만 테스트하기 때문에 MainThread를 사용할 수 없어 에러 발생
    // InstantTaskExecutorRole을 사용하여 isMainThread()가 true를 반환하도록 설정
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // 2. test viewModel fetch
    // 입력된 데이터 검증
    @Test
    fun `test viewModel fetch`(): Unit = runBlockingTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.fetchData()
        testObservable.assertValueSequence(
            listOf(
                TodoListState.UnInitialized,
                TodoListState.Loading,
                TodoListState.Success(mockList)
            )
        )
    }

    // 3. test Item Update
    // 데이터 업데이트 반영 검증
    @Test
    fun `test Item Update`(): Unit = runBlockingTest {
        val todo = TodoEntity(
            id = 1,
            title = "title 1",
            description = "description 1",
            hasCompleted = true
        )
        viewModel.updateEntity(todo)
        assert((getTodoItemUseCase(todo.id)?.hasCompleted ?: false) == todo.hasCompleted)
    }

    // 4. test Item Delete All
    // 데이터 초기화 검증
    @Test
    fun `test Item Delete All`(): Unit = runBlockingTest {
        val testObservable = viewModel.todoListLiveData.test()
        viewModel.deleteAll()
        testObservable.assertValueSequence(
            listOf(
                TodoListState.UnInitialized,
                TodoListState.Loading,
                TodoListState.Success(listOf())
            )
        )
    }
}