package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.data.CategoriesDataSource
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

class ProductCategoriesLocalDataSource(private val categoriesDao: ProductCategoriesDao) : CategoriesDataSource {

    override fun saveCategory(category: ProductCategory): Boolean =
            categoriesDao.saveProductCategory(category) > 0L

    override fun loadCategories(): List<ProductCategory> = categoriesDao.getAllCategories()
}