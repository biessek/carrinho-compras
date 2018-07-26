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
import rocks.biessek.testemeuspedidos.data.model.ProductCategory
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@SmallTest
class ProductCategoriesLocalDataSourceTest {
    private lateinit var database: ProductsDatabase
    private lateinit var categoriesDao: ProductCategoriesDao

    private val testCategory = ProductCategory(1L, "Categoria teste")

    private lateinit var categoriesLocalDataSource: ProductCategoriesLocalDataSource

    @Before
    fun createDataSource() {
        val context = InstrumentationRegistry.getTargetContext()
        database = Room.inMemoryDatabaseBuilder(context, ProductsDatabase::class.java).build()
        categoriesDao = database.categoriesDao()

        categoriesLocalDataSource = ProductCategoriesLocalDataSource(categoriesDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    fun checkCanSaveProductCategory() {
        categoriesDao.deleteProductCategories(testCategory)

        val result = categoriesLocalDataSource.saveCategory(testCategory)

        assertTrue(result)
    }

    @Test
    fun checkCanSaveProductCategories() {
        categoriesDao.deleteProductCategories(testCategory)

        val result = categoriesLocalDataSource.saveAllCategories(arrayOf(testCategory, ProductCategory(2, "Test 2")))

        assertTrue(result)
    }

    @Test
    fun checkCanListProductCategories() {
        categoriesDao.saveProductCategory(testCategory)

        val allCategories = categoriesLocalDataSource.loadCategories()

        assertTrue(allCategories.isNotEmpty())
    }

}