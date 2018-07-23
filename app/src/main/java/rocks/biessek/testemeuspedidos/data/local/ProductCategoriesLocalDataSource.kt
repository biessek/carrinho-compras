package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.data.CategoriesDataSource
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

class ProductCategoriesLocalDataSource(private val categoriesDao: ProductCategoriesDao) : CategoriesDataSource {

    override fun saveCategory(category: ProductCategory): Boolean {
        return false
    }

    override fun loadCategories(): List<ProductCategory> = categoriesDao.getAllCategories()
}