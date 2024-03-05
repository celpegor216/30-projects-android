package fastcampus.aop.pjt30_food_delivery.livedata

import androidx.lifecycle.Observer
import java.lang.AssertionError

// 값이 변경되었을 때 비교하는 로직
class LiveDataTestObserver<T>: Observer<T> {

    private val values: MutableList<T> = mutableListOf()

    override fun onChanged(value: T) {
        values.add(value)
    }

    fun assertValueSequence(sequence: List<T>): LiveDataTestObserver<T> {
        var i = 0
        val actualIterator = values.iterator()
        val expectedIterator = sequence.iterator()

        var actualNext: Boolean
        var expectedNext: Boolean

        while (true) {
            actualNext = actualIterator.hasNext()
            expectedNext = expectedIterator.hasNext()

            if (!actualNext || !expectedNext) break

            val actual: T = actualIterator.next()
            val expected: T = expectedIterator.next()

            if (actual != expected) {
                throw AssertionError("actual: $actual, expected: $expected, index: $i")
            }

            i++
        }

        if (actualNext) {
            throw AssertionError("More values received than expected ($i")
        }

        if (expectedNext) {
            throw AssertionError("Fewer values received than expected ($i")
        }

        return this
    }
}