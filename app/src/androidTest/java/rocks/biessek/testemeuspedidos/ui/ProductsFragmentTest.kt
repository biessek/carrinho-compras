package rocks.biessek.testemeuspedidos.ui

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
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
import rocks.biessek.testemeuspedidos.ui.adapter.CategoryViewHolder
import rocks.biessek.testemeuspedidos.ui.adapter.ProductsViewHolder
import rocks.biessek.testemeuspedidos.ui.model.Product
import rocks.biessek.testemeuspedidos.ui.model.ProductCategory
import rocks.biessek.testemeuspedidos.ui.utils.RecyclerViewMatcher
import java.io.IOException


@RunWith(AndroidJUnit4::class)
@LargeTest
class ProductsFragmentTest {
    private lateinit var database: ProductsDatabase
    private lateinit var server: MockWebServer

    @get:Rule
    val activityRule: ActivityTestRule<MainActivity> = ActivityTestRule(
            MainActivity::class.java, false, false)

    @Before
    fun createRepo() {
        val context = InstrumentationRegistry.getTargetContext()
        IdlingRegistry.getInstance().register(AppIdlingResource.countingIdlingResource)
        val kodein = (context.applicationContext as TestApp).kodein.direct
        database = kodein.instance()
        server = kodein.instance()
        database.clearAllTables()
    }

    @Test
    fun checkEmptyListProducts() {
        withContent(noResponse(), noResponse())
        activityRule.launchActivity(null)
        onData(`is`(instanceOf(Product::class.java)))
        val emptyText = activityRule.activity.resources.getString(R.string.no_products)
        onView(withText(emptyText)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun checkListProducts() {
        withContent(someProducts(), noResponse())
        activityRule.launchActivity(null)

        onData(`is`(instanceOf(Product::class.java)))
        onView(withId(R.id.products_list)).check(ViewAssertions.matches(hasChildCount(3)))
    }

    @Test
    fun checkShowCategoriesMenu() {
        withContent(someProducts(), someCategories())
        activityRule.launchActivity(null)

        onView(withId(R.id.drawer_menu)).perform(click())
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(Gravity.RIGHT)))

        onData(`is`(instanceOf(ProductCategory::class.java)))
        onView(withId(R.id.categories_list)).check(ViewAssertions.matches(hasChildCount(3)))
    }


    @Test
    fun checkFilterByCategory() {
        withContent(someProducts(), someCategories())
        activityRule.launchActivity(null)
        onData(`is`(instanceOf(ProductCategory::class.java)))

        onView(withId(R.id.drawer_menu)).perform(click())
        onView(withId(R.id.categories_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition<CategoryViewHolder>(1, click()))

        onData(`is`(instanceOf(Product::class.java)))
        onView(withId(R.id.products_list)).check(ViewAssertions.matches(hasChildCount(2)))
        assertCategorySelection()
    }

    private fun assertCategorySelection() {
        onView(withId(R.id.drawer_menu)).perform(click())
        for (i in 0..2) {
            if (i == 1) {
                onView(allOf(
                        withParent(nthChildOf(withId(R.id.categories_list), i)),
                        withId(R.id.checkbox)
                )).check(matches(isDisplayed()))
            } else {
                onView(allOf(
                        withParent(nthChildOf(withId(R.id.categories_list), i)),
                        withId(R.id.checkbox)
                )).check(matches(not(isDisplayed())))
            }
        }

    }

    @Test
    fun checkCanFavoriteProduct() {
        withContent(someProducts(), someCategories())
        activityRule.launchActivity(null)

        onView(withId(R.id.products_list)).perform(scrollToPosition<ProductsViewHolder>(2))

        onView(withRecyclerView(R.id.products_list).atPosition(2))
                .check(matches(hasDescendant(withText("Galaxy A5 2016"))))
                .check(matches(hasDescendant(allOf(withId(R.id.favorite), isNotChecked()))))
        onView(allOf(
                withId(R.id.favorite),
                withParent(hasDescendant(withText("Galaxy A5 2016")))
        )).perform(click())

        activityRule.finishActivity()
        activityRule.launchActivity(null)

        onView(withRecyclerView(R.id.products_list).atPosition(2))
                .check(matches(hasDescendant(withText("Galaxy A5 2016"))))
                .check(matches(hasDescendant(allOf(withId(R.id.favorite), isChecked()))))
    }

    @Test
    fun checkNavigateToDetails() {
        val testDescription = "Alta performance Hardware de alta qualidade e performance para navegação na internet. O processador Octa-Core permite carregamento de páginas de navegadores instantaneamente, transições de interfaces de maneira suave e multitarefa. O Galaxy A permite também a expansão de memória."
        withContent(someProducts(), someCategories())
        activityRule.launchActivity(null)

        onView(withId(R.id.products_list)).perform(scrollToPosition<ProductsViewHolder>(2))

        onView(withRecyclerView(R.id.products_list).atPosition(2)).perform(click())

        onView(withText(testDescription)).check(matches(isDisplayed()))
    }

    @Test
    fun checkFavoriteProductFromDetails() {
        withContent(someProducts(), someCategories())
        activityRule.launchActivity(null)

        onView(withId(R.id.products_list)).perform(scrollToPosition<ProductsViewHolder>(2))

        onView(withRecyclerView(R.id.products_list).atPosition(2)).perform(click())

        onView(withId(R.id.favorite)).check(matches(isNotChecked()))
        onView(withId(R.id.favorite)).perform(click())
        onView(withId(R.id.favorite)).check(matches(isChecked()))

        pressBack()
        onView(withRecyclerView(R.id.products_list).atPosition(2))
                .check(matches(hasDescendant(withText("Galaxy A5 2016"))))
                .check(matches(hasDescendant(allOf(withId(R.id.favorite), isChecked()))))
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(AppIdlingResource.countingIdlingResource)
    }

    private val PRODUCTS_PATH = "/b95b75cfddc6b1cb601d7f806859e1dc/raw/dc973df65664f6997eeba30158d838c4b716204c/products.json"
    private val CATEGORIES_PATH = "/e84d0d969613fd0ef8f9fd08546f7155/raw/a0611f7e765fa2b745ad9a897296e082a3987f61/categories.json"

    private fun noResponse(): MockResponse {
        return MockResponse().setResponseCode(404)
    }

    private fun withContent(productsResponse: MockResponse, categoriesResponse: MockResponse) {
        server.setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest?): MockResponse {
                if (request?.path == PRODUCTS_PATH) {
                    return productsResponse
                } else if (request?.path == CATEGORIES_PATH) {
                    return categoriesResponse
                }
                return noResponse()
            }
        })
    }

    private fun someProducts(): MockResponse {
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
        },
        {
            "name": "Galaxy A5 2016",
            "description": "Alta performance Hardware de alta qualidade e performance para navegação na internet. O processador Octa-Core permite carregamento de páginas de navegadores instantaneamente, transições de interfaces de maneira suave e multitarefa. O Galaxy A permite também a expansão de memória.",
            "photo": "https://simplest-meuspedidos-arquivos.s3.amazonaws.com/media/imagem_produto/133421/07b3b1d8-48f8-11e6-aa97-020adee616d7.jpeg",
            "price": 1979.10,
            "category_id": 2
        }
    ]
    """
        return MockResponse().setBody(testJsonProducts)
    }

    private fun someCategories(): MockResponse {
        val testJsonCategories = """
    [
      {
        "id": 1,
        "name": "Televisores"
      },
      {
        "id": 2,
        "name": "Celulares"
      }
    ]
    """
        return MockResponse().setBody(testJsonCategories)
    }


    fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("position $childPosition of parent ")
                parentMatcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) return false
                val parent = view.parent as ViewGroup

                return (parentMatcher.matches(parent)
                        && parent.childCount > childPosition
                        && parent.getChildAt(childPosition) == view)
            }
        }
    }

    fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
        return RecyclerViewMatcher(recyclerViewId)
    }

}