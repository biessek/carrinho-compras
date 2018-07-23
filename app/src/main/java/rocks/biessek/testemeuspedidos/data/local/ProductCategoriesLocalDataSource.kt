package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.domain.CategoriesDataSource
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

class ProductCategoriesLocalDataSource(private val categoriesDao: ProductCategoriesDao) : CategoriesDataSource {

    override fun saveCategory(category: ProductCategory): Boolean {
        return categoriesDao.saveProductCategory(
                rocks.biessek.testemeuspedidos.data.model.ProductCategory(
                        category.id,
                        category.name
                )
        ) > 0L
    }

    override fun loadCategories(): List<ProductCategory> = categoriesDao.getAllCategories()
}