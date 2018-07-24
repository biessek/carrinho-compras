package rocks.biessek.testemeuspedidos.ui

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.data.ProductsRepository
import rocks.biessek.testemeuspedidos.data.local.ProductsDao
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.data.local.ProductsLocalDataSource
import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.remote.ProductsRemoteDataSource
import rocks.biessek.testemeuspedidos.data.remote.ServiceApi
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@LargeTest
class ProductsFragmentTest {
    private lateinit var database: ProductsDatabase
    private lateinit var productsDao: ProductsDao
    private lateinit var retrofit: Retrofit
    private lateinit var server: MockWebServer

    private lateinit var productsRepository: ProductsRepository
    private val testProduct = Product(name = "Produto Teste", description = "Produto criado para o teste", photo = "foto.png", price = 33.99, categoryId = 1L)

    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java)

    @Before
    fun createRepo() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, ProductsDatabase::class.java).build()
        productsDao = database.productsDao()

        server = MockWebServer()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url(""))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        productsRepository = ProductsRepository(
                ProductsLocalDataSource(productsDao),
                ProductsRemoteDataSource(retrofit.create(ServiceApi::class.java))
        )
    }

    @Test
    fun checkEmptyListProducts() {
        withNoProducts()

        val emptyText = activityRule.activity.resources.getString(R.string.no_products)
        onView(withText(emptyText)).check(ViewAssertions.matches(isDisplayed()))
    }

    private fun withNoProducts() {
        database.clearAllTables()
    }

    @Test
    fun checkListProducts() {

    }

    //@Test
    fun checkShowCategoriesMenu() {

    }

    //@Test
    fun checkSearchCategoryByTiping() {

    }

    //@Test
    fun checkFilterByCategory() {

    }

    //@Test
    fun checkNavigateToDetails() {

    }

    @After
    @Throws(IOException::class)
    fun closeRepo() {
        server.shutdown()
        database.close()
    }

}