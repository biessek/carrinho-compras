package rocks.biessek.testemeuspedidos.data.remote

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ProductsRemoteDataSourceTest {
    private lateinit var retrofit: Retrofit
    private lateinit var server: MockWebServer
    private lateinit var productsRemoteDataSource: ProductsRemoteDataSource

    private val testJson = """
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
    fun createServer() {
        server = MockWebServer()
        retrofit = Retrofit.Builder()
                .baseUrl(server.url(""))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        productsRemoteDataSource = ProductsRemoteDataSource(retrofit.create(ServiceApi::class.java))
        server.start()
    }

    @After
    fun shutdownServer() {
        server.shutdown()
    }

    @Test
    fun checkCanListProducts() {
        server.enqueue(MockResponse().setBody(testJson))

        val list = productsRemoteDataSource.loadProducts()

        assertEquals(1, list.size)
    }

    @Test
    fun checkEmptyListOnError() {
        server.enqueue(MockResponse().setResponseCode(404))

        val list = productsRemoteDataSource.loadProducts()

        assert(list.isEmpty())
    }

}