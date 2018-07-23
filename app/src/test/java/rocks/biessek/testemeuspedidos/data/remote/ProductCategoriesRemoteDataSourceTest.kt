package rocks.biessek.testemeuspedidos.data.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductCategoriesRemoteDataSourceTest {
    private lateinit var retrofit: Retrofit
    private lateinit var server: MockWebServer
    private lateinit var categoriesRemoteDataSource: ProductCategoriesRemoteDataSource

    private val testJson = """
    [
      {
        "id": 1,
        "name": "Televisores"
      }
    ]
    """

    @Before
    fun createServer() {
        server = MockWebServer()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url(""))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        categoriesRemoteDataSource = ProductCategoriesRemoteDataSource(retrofit.create(ServiceApi::class.java))
        server.start()
    }

    @After
    fun shutdownServer() {
        server.shutdown()
    }

    @Test
    fun checkCanListProductCategories() {
        server.enqueue(MockResponse().setBody(testJson))

        val list = categoriesRemoteDataSource.loadCategories()

        assertEquals(1, list.size)
    }

    @Test
    fun checkEmptyListOnError() {
        server.enqueue(MockResponse().setResponseCode(404))

        val list = categoriesRemoteDataSource.loadCategories()

        assert(list.isEmpty())
    }

}