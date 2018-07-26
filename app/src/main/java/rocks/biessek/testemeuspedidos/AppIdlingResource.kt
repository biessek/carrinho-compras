package rocks.biessek.testemeuspedidos

import androidx.test.espresso.idling.CountingIdlingResource

object AppIdlingResource {
    private const val tag = "app_idling"
    val countingIdlingResource = CountingIdlingResource(tag)

    fun increment() {
        countingIdlingResource.increment()
    }
    fun decrement() {
        countingIdlingResource.dumpStateToLogs()
        if(!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}