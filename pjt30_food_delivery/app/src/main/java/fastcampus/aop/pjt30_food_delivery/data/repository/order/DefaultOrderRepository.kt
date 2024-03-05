package fastcampus.aop.pjt30_food_delivery.data.repository.order

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import fastcampus.aop.pjt30_food_delivery.data.entity.OrderEntity
import fastcampus.aop.pjt30_food_delivery.data.entity.RestaurantFoodEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DefaultOrderRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val firestore: FirebaseFirestore
) : OrderRepository {

    override suspend fun orderMenu(
        userId: String,
        restaurantId: Long,
        restaurantTitle: String,
        foodMenuList: List<RestaurantFoodEntity>
    ): Result = withContext(ioDispatcher) {
        val result: Result
        val orderMenuData = hashMapOf(
            "userId" to userId,
            "restaurantId" to restaurantId,
            "restaurantTitle" to restaurantTitle,
            "orderMenuList" to foodMenuList
        )
        result = try {
            firestore.collection(COLLECTION_ORDER).add(orderMenuData)
            Result.Success<Any>()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

        return@withContext result
    }

    override suspend fun getAllOrderMenu(userId: String): Result = withContext(ioDispatcher) {
        return@withContext try {
            val result: QuerySnapshot = firestore
                .collection(COLLECTION_ORDER)
                .whereEqualTo("userId", userId)
                .get()
                .await()
            Result.Success(result.documents.map {
                OrderEntity(
                    id = it.id,
                    userId = it.get("userId") as String,
                    restaurantId = it.get("restaurantId") as Long,
                    restaurantTitle = it.get("restaurantTitle") as String,
                    foodMenuList = (it.get("orderMenuList") as ArrayList<Map<String, Any>>).map { food ->
                        RestaurantFoodEntity(
                            id = food["id"] as String,
                            title = food["title"] as String,
                            price = (food["price"] as Long).toInt(),
                            imageUrl = food["imageUrl"] as String,
                            restaurantId = food["restaurantId"] as Long,
                            restaurantTitle = food["restaurantTitle"] as String,
                            description = food["description"] as String
                        )
                    }
                )
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }
    }

    sealed class Result {
        data class Success<T>(
            val data: T? = null
        ) : Result()

        data class Error(
            val e: Throwable
        ) : Result()
    }

    companion object {
        private const val COLLECTION_ORDER = "order"
    }
}