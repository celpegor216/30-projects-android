package fastcampus.aop.pjt20_github_repository

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.system.measureTimeMillis

class CoroutinesTest01 {

    @Test
    // getFirstName, getLastName 각각 호출할 때마다 중단되어 총 2초가 걸림
    fun testRunBlocking() = runBlocking {
        val time = measureTimeMillis {
            val firstName = getFirstName()
            val lastName = getLastName()
            println("testRunBlocking Hello, $firstName $lastName")
        }
        println("testRunBlocking time: $time")
    }

    @Test
    // getFirstName, getLastName 동시에 호출하여 총 1초가 걸림
    fun testAsync() = runBlocking {
        val time = measureTimeMillis {
            val firstName = async { getFirstName() }
            val lastName = async { getLastName() }
            println("testAsync Hello, ${firstName.await()} ${lastName.await()}")
        }
        println("testAsync time: $time")
    }

    suspend fun getFirstName(): String {
        delay(1000)
        return "조"
    }

    suspend fun getLastName(): String {
        delay(1000)
        return "예지"
    }
}