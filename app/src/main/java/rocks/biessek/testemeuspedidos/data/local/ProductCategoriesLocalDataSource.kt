package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.domain.CategoriesDataSource
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

class ProductCategoriesLocalDataSource(private val categoriesDao: ProductCategoriesDao) : CategoriesDataSource {
    override fun saveCategory(category: ProductCategory): Boolean {
        return categoriesDao.saveProductCategory(
                toDataCategory(category)
        ) > 0L
    }

    override fun saveAllCategories(categories: Array<ProductCategory>): Boolean {
        val ids = categoriesDao.saveAllCategories(categories.map { toDataCategory(it) }.toTypedArray())
        return ids.size == categories.size
    }

    override fun loadCategories(): List<ProductCategory> = categoriesDao.getAllCategories()

    private fun toDataCategory(category: ProductCategory): rocks.biessek.testemeuspedidos.data.model.ProductCategory {
        return rocks.biessek.testemeuspedidos.data.model.ProductCategory(
                category.id,
                category.name
        )
    }

}