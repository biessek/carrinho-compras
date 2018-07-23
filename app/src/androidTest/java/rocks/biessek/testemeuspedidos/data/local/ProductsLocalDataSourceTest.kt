package rocks.biessek.testemeuspedidos.data.local

import androidx.room.Room
import androidx.test.InstrumentationRegistry
import androidx.test.filters.SmallTest
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductsLocalDataSourceTest {
    private lateinit var database: ProductsDatabase
    private lateinit var productsDao: ProductsDao

    private val testProduct = Product(name = "Produto Teste", description = "Produto criado para o teste", photo = "foto.png", price = 33.99, categoryId = 1L)
    private val testCategory = ProductCategory(1L, "Categoria teste")

    private lateinit var productsLocalDataSource: ProductsLocalDataSource

    @Before
    fun createDataSource() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, ProductsDatabase::class.java).build()
        productsDao = database.productsDao()

        productsLocalDataSource = ProductsLocalDataSource(productsDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun checkCanListProducts() {
        productsDao.saveProduct(testProduct)

        val allProducts = productsLocalDataSource.loadProducts()
        assertTrue(allProducts.isNotEmpty())
    }

    @Test
    fun checkCanListProductFromCategory() {
        productsDao.saveProduct(testProduct)

        val result = productsLocalDataSource.loadProductsFromCategory(testCategory)

        assertTrue(result.isNotEmpty())
    }

    @Test
    fun checkCanSaveProduct() {
        val result = productsLocalDataSource.saveProduct(testProduct)

        assertTrue(result)
    }

}