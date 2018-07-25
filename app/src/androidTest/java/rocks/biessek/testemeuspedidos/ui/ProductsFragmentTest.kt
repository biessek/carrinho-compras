package rocks.biessek.testemeuspedidos.ui

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.direct
import org.kodein.di.generic.instance
import rocks.biessek.testemeuspedidos.AppIdlingResource
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.TestApp
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.ui.model.Product
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@LargeTest
class ProductsFragmentTest {
    private lateinit var database: ProductsDatabase
    private lateinit var server: MockWebServer

    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java)

    @Before
    fun createRepo() {
        val context = InstrumentationRegistry.getTargetContext()
        val kodein = (context.applicationContext as TestApp).kodein.direct
        database = kodein.instance()
        server = kodein.instance()

        IdlingRegistry.getInstance().register(AppIdlingResource.countingIdlingResource)
    }

    //    @Test
    fun checkEmptyListProducts() {
        withNoProducts()

        val emptyText = activityRule.activity.resources.getString(R.string.no_products)
        onView(withText(emptyText)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun checkListProducts() {
        withSomeProducts()

        onData(`is`(instanceOf(Product::class.java)))
        onView(withId(R.id.products_list)).check(ViewAssertions.matches(hasChildCount(2)))
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
        IdlingRegistry.getInstance().unregister(AppIdlingResource.countingIdlingResource)
        server.shutdown()
        database.close()
    }

    private fun withNoProducts() {
        database.clearAllTables()
        server.enqueue(MockResponse().setResponseCode(404))
    }

    private fun withSomeProducts() {
        val testJsonProducts = """
    [
        {
            "name": "32\" Full HD Flat Smart TV H5103 Series 3",
            "description": "Com o Modo futebol, é como se você estivesse realmente no jogo. Ele exibe, de forma precisa e viva, a grama verde do campo e todas as outras cores do estádio. Um poderoso efeito de som multi-surround também permite que você ouça toda a empolgação. Você pode até mesmo ampliar áreas selecionadas da tela para uma melhor visualização. Com apenas o toque de um botão, você pode aproveitar ao máximo o seu esporte favorito com todos os seus amigos.",
            "photo": "https://simplest-meuspedidos-arquivos.s3.amazonaws.com/media/imagem_produto/133421/fda44b12-48f7-11e6-996c-0aad52ea90db.jpeg",
            "price": 1466.10,
            "category_id": 1
        },
        {
            "name": "40\" Full HD Flat Smart TV H5103 Series 5",
            "description": "Usando um algoritmo avançado de melhoria da qualidade de imagem, o Wide Color Enhancer Plus da Samsung melhora consideravelmente a qualidade de qualquer imagem e revela detalhes ocultos. Com o Wide Color Enhancer Plus, agora você verá as cores como elas realmente devem ser vistas.",
            "photo": "https://simplest-meuspedidos-arquivos.s3.amazonaws.com/media/imagem_produto/133421/fe41bc44-48f7-11e6-a3ac-0a9a90ee83e3.jpeg",
            "price": 1979.10,
            "category_id": 1
        }
    ]
    """
        database.clearAllTables()
        server.enqueue(MockResponse().setBody(testJsonProducts))
    }


}