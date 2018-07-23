package rocks.biessek.testemeuspedidos.data

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.filters.MediumTest
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rocks.biessek.testemeuspedidos.data.local.ProductCategoriesDao
import rocks.biessek.testemeuspedidos.data.local.ProductCategoriesLocalDataSource
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.data.model.ProductCategory
import rocks.biessek.testemeuspedidos.data.remote.ProductCategoriesRemoteDataSource
import rocks.biessek.testemeuspedidos.data.remote.ServiceApi
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@MediumTest
class ProductCategoriesRepositoryTest {
    private lateinit var database: ProductsDatabase
    private lateinit var categoriesDao: ProductCategoriesDao

    private lateinit var retrofit: Retrofit
    private lateinit var server: MockWebServer

    private lateinit var categoriesRepository: ProductCategoriesRepository


    private val testCategory = ProductCategory(1L, "Categoria teste")
    private val testJsonCategory = """
    [
      {
        "id": 1,
        "name": "Televisores"
      }
    ]
    """


    @Before
    fun createRepo() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, ProductsDatabase::class.java).build()
        categoriesDao = database.categoriesDao()

        server = MockWebServer()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url(""))
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        categoriesRepository = ProductCategoriesRepository(
                ProductCategoriesLocalDataSource(categoriesDao),
                ProductCategoriesRemoteDataSource(retrofit.create(ServiceApi::class.java))
        )
    }

    @Test
    fun checkCantListFromAnywhere() {
        server.enqueue(MockResponse().setResponseCode(404))
        database.clearAllTables()

        val list = categoriesRepository.loadCategories()

        assert(list.isEmpty())
    }

    @Test
    fun checkCanListFromLocal() {
        server.enqueue(MockResponse().setResponseCode(404))
        categoriesDao.saveProductCategory(testCategory)

        val list = categoriesRepository.loadCategories()

        assertEquals(1, list.size)
    }

    @Test
    fun checkCanListFromLocalAfterCache() {
        server.enqueue(MockResponse().setBody(testJsonCategory))

        var list = categoriesRepository.loadCategories()
        assertEquals(1, list.size)

        server.enqueue(MockResponse().setResponseCode(404))

        list = categoriesRepository.loadCategories()
        assertEquals(1, list.size)
    }

    @Test
    fun checkCanListFromRemote() {
        server.enqueue(MockResponse().setBody(testJsonCategory))
        database.clearAllTables()

        val list = categoriesRepository.loadCategories()

        assertEquals(1, list.size)
    }

    @After
    @Throws(IOException::class)
    fun closeRepo() {
        server.shutdown()
        database.close()
    }

}