package fastcampus.aop.pjt30_food_delivery.viewmodel.restaurant

import fastcampus.aop.pjt30_food_delivery.data.entity.LocationLatLngEntity
import fastcampus.aop.pjt30_food_delivery.data.repository.restaurant.RestaurantRepository
import fastcampus.aop.pjt30_food_delivery.model.restaurant.RestaurantModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantCategory
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantListViewModel
import fastcampus.aop.pjt30_food_delivery.screen.main.home.restaurant.RestaurantOrder
import fastcampus.aop.pjt30_food_delivery.viewmodel.ViewModelTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

@ExperimentalCoroutinesApi
internal class RestaurantListViewModelTest : ViewModelTest() {

    private var restaurantCategory = RestaurantCategory.ALL
    private val locationLatLngEntity = LocationLatLngEntity(latitude = 0.0, longitude = 0.0)

    private val restaurantRepository by inject<RestaurantRepository>()

    private val restaurantListViewModel by inject<RestaurantListViewModel> {
        parametersOf(restaurantCategory, locationLatLngEntity)
    }

    @Test
    fun `test load restaurant list category ALL`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).map {
                    RestaurantModel(
                        id = it.id,
                        restaurantTitle = it.restaurantTitle,
                        restaurantCategory = it.restaurantCategory,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantImageUrl = it.restaurantImageUrl,
                        restaurantTelNumber = it.restaurantTelNumber,
                        reviewCount = it.reviewCount,
                        grade = it.grade,
                        deliveryTipRange = it.deliveryTipRange,
                        deliveryTimeRange = it.deliveryTimeRange
                    )
                }
            )
        )
    }

    @Test
    fun `test load restaurant list category Excepted`() = runBlockingTest {
        restaurantCategory = RestaurantCategory.CAFE_DESSERT

        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.fetchData()

        testObservable.assertValueSequence(
            listOf(
                listOf()
            )
        )
    }

    @Test
    fun `test load restaurant list order by fast delivery time`() = runBlockingTest {
        val testObservable = restaurantListViewModel.restaurantListLiveData.test()

        restaurantListViewModel.setRestaurantOrder(RestaurantOrder.FAST_DELIVERY)

        testObservable.assertValueSequence(
            listOf(
                restaurantRepository.getList(restaurantCategory, locationLatLngEntity).sortedBy { it.deliveryTimeRange.first }.map {
                    RestaurantModel(
                        id = it.id,
                        restaurantTitle = it.restaurantTitle,
                        restaurantCategory = it.restaurantCategory,
                        restaurantInfoId = it.restaurantInfoId,
                        restaurantImageUrl = it.restaurantImageUrl,
                        restaurantTelNumber = it.restaurantTelNumber,
                        reviewCount = it.reviewCount,
                        grade = it.grade,
                        deliveryTipRange = it.deliveryTipRange,
                        deliveryTimeRange = it.deliveryTimeRange
                    )
                }
            )
        )
    }
}