package fastcampus.aop.pjt30_food_delivery.viewmodel.order

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import fastcampus.aop.pjt30_food_delivery.data.entity.OrderEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.order.DefaultOrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.order.OrderRepository
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.food.RestaurantFoodRepository
import fastcampus.aop.pjt30_food_delivery.model.CellType
import fastcampus.aop.pjt30_food_delivery.model.restaurant.food.FoodModel
import fastcampus.aop.pjt30_food_delivery.screen.order.OrderMenuListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.order.OrderMenuState
import fastcampus.aop.pjt30_food_delivery.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
internal class OrderMenuListViewModelTest : ViewModelTest() {

    @Mock
    lateinit var firebaseAuth: FirebaseAuth

    @Mock
    lateinit var firebaseUser: FirebaseUser

    private val orderMenuListViewModel by inject<OrderMenuListViewModel> {
        parametersOf(firebaseAuth)
    }

    private val restaurantFoodRepository by inject<RestaurantFoodRepository>()
    private val orderRepository by inject<OrderRepository>()

    private val restaurantId = 0L
    private val restaurantTitle = "식당명"

    // 장바구니에 음식 추가하기
    @Test
    fun `insert food menu in basket`() = runBlockingTest {
        (0 until 10).forEach {
            restaurantFoodRepository.insertFoodMenuIntoBasket(
                RestaurantFoodEntity(
                    id = it.toString(),
                    title = "메뉴 $it",
                    description = "소개 $it",
                    price = it,
                    imageUrl = "",
                    restaurantId = restaurantId,
                    restaurantTitle = restaurantTitle
                )
            )
        }
        assert(restaurantFoodRepository.getFoodMenuListInBasket().size == 10)
    }

    // 장바구니에 추가된 음식 목록 가져오기
    @Test
    fun `test load order menu list`() = runBlockingTest {
        `insert food menu in basket`()

        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()

        orderMenuListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.Uninitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelList = restaurantFoodRepository.getFoodMenuListInBasket()
                        .map {
                            FoodModel(
                                id = it.hashCode().toLong(),
                                type = CellType.ORDER_FOOD_CELL,
                                title = it.title,
                                description = it.description,
                                price = it.price,
                                imageUrl = it.imageUrl,
                                restaurantId = it.restaurantId,
                                foodId = it.id,
                                restaurantTitle = it.restaurantTitle
                            )
                        }
                )
            )
        )
    }

    // 장바구니에 추가된 음식들을 주문하기
    @Test
    fun `test order menu list`() = runBlockingTest {
        `insert food menu in basket`()

        val userId = "test"

        Mockito.`when`(firebaseAuth.currentUser).then { firebaseUser }
        Mockito.`when`(firebaseUser.uid).then { userId }

        val testObservable = orderMenuListViewModel.orderMenuStateLiveData.test()

        orderMenuListViewModel.fetchData()

        val menuListInBasket = restaurantFoodRepository.getFoodMenuListInBasket().map { it.copy() }

        val menuListInBasketModelList = menuListInBasket.map {
            FoodModel(
                id = it.hashCode().toLong(),
                type = CellType.ORDER_FOOD_CELL,
                title = it.title,
                description = it.description,
                price = it.price,
                imageUrl = it.imageUrl,
                restaurantId = it.restaurantId,
                foodId = it.id,
                restaurantTitle = it.restaurantTitle
            )
        }

        orderMenuListViewModel.orderMenu()

        testObservable.assertValueSequence(
            listOf(
                OrderMenuState.Uninitialized,
                OrderMenuState.Loading,
                OrderMenuState.Success(
                    restaurantFoodModelList = menuListInBasketModelList
                ),
                OrderMenuState.Order
            )
        )

        assert(orderRepository.getAllOrderMenu(userId) is DefaultOrderRepository.Result.Success<*>)

        val result = (orderRepository.getAllOrderMenu(userId) as DefaultOrderRepository.Result.Success<*>).data

        assert(result?.equals(
            listOf(
                OrderEntity(
                    id = 0.toString(),
                    userId = userId,
                    restaurantId = restaurantId,
                    foodMenuList = menuListInBasket,
                    restaurantTitle = restaurantTitle
                )
            )
        ) ?: false)
    }
}