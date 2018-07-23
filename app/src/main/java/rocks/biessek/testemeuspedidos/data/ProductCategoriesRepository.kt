package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.domain.CategoriesDataSource
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

class ProductCategoriesRepository(private val localCategoriesDataSource: CategoriesDataSource,
                                  private val remoteCategoriesDataSource: CategoriesDataSource) : CategoriesDataSource {
    override fun saveCategory(category: ProductCategory): Boolean = localCategoriesDataSource.saveCategory(category)

    override fun loadCategories(): List<ProductCategory> {
        val localResult = localCategoriesDataSource.loadCategories()

        if (localResult.isNotEmpty()) {
            return localResult
        }
        val remoteResult = remoteCategoriesDataSource.loadCategories()
        remoteResult.forEach { saveCategory(it) }
        return remoteResult

    }
}