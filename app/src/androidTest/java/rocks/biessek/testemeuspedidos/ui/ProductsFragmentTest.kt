package rocks.biessek.testemeuspedidos.ui

import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import kotlinx.android.synthetic.main.fragment_products.view.*
import org.junit.Test
import rocks.biessek.testemeuspedidos.R


@RunWith(AndroidJUnit4::class)
@LargeTest
class ProductsFragmentTest {
    @Rule
    var mActivityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java)


    @Test
    fun checkEmptyListProducts() {

    }

    @Test
    fun checkListProducts() {

    }

    @Test
    fun checkShowCategoriesMenu() {

    }

    @Test
    fun checkSearchCategoryByTiping() {

    }
    @Test
    fun checkFilterByCategory() {

    }

    @Test
    fun checkNavigateToDetails() {

    }
}