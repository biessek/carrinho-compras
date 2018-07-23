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
import rocks.biessek.testemeuspedidos.data.local.ProductsDao
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.data.local.ProductsLocalDataSource
import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory
import rocks.biessek.testemeuspedidos.data.remote.ProductsRemoteDataSource
import rocks.biessek.testemeuspedidos.data.remote.ServiceApi
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@MediumTest
class ProductsRepositoryTest {
    private lateinit var database: ProductsDatabase
    private lateinit var productsDao: ProductsDao

    private lateinit var retrofit: Retrofit
    private lateinit var server: MockWebServer

    private lateinit var productsRepository: ProductsRepository


    private val testProduct = Product(name = "Produto Teste", description = "Produto criado para o teste", photo = "foto.png", price = 33.99, categoryId = 1L)
    private val testCategory = ProductCategory(1L, "Categoria teste")
    private val testJsonProduct = """
    [
        {
            "name": "32\" Full HD Flat Smart TV H5103 Series 3",
            "description": "Com o Modo futebol, é como se você estivesse realmente no jogo. Ele exibe, de forma precisa e viva, a grama verde do campo e todas as outras cores do estádio. Um poderoso efeito de som multi-surround também permite que você ouça toda a empolgação. Você pode até mesmo ampliar áreas selecionadas da tela para uma melhor visualização. Com apenas o toque de um botão, você pode aproveitar ao máximo o seu esporte favorito com todos os seus amigos.",
            "photo": "https://simplest-meuspedidos-arquivos.s3.amazonaws.com/media/imagem_produto/133421/fda44b12-48f7-11e6-996c-0aad52ea90db.jpeg",
            "price": 1466.10,
            "category_id": 1
        }
    ]
    """


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
    fun checkCantListFromAnywhere() {
        server.enqueue(MockResponse().setResponseCode(404))
        database.clearAllTables()

        val list = productsRepository.loadProducts()

        assert(list.isEmpty())
    }

    @Test
    fun checkCanListFromLocal() {
        server.enqueue(MockResponse().setResponseCode(404))
        productsDao.saveProduct(testProduct)

        val list = productsRepository.loadProducts()

        assertEquals(1, list.size)
    }

    @Test
    fun checkCanListFromCategoryFromLocal() {
        server.enqueue(MockResponse().setResponseCode(404))
        productsDao.saveProduct(testProduct)

        val list = productsRepository.loadProductsFromCategory(testCategory)

        assertEquals(1, list.size)
    }

    @Test
    fun checkCantListFromCategoryFromRemote() {
        server.enqueue(MockResponse().setBody(testJsonProduct))
        database.clearAllTables()

        val list = productsRepository.loadProductsFromCategory(testCategory)

        assert(list.isEmpty())
    }

    @Test
    fun checkCanListFromRemote() {
        server.enqueue(MockResponse().setBody(testJsonProduct))
        database.clearAllTables()

        val list = productsRepository.loadProducts()

        assertEquals(1, list.size)
    }

    @After
    @Throws(IOException::class)
    fun closeRepo() {
        server.shutdown()
        database.close()
    }

}