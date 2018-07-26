package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

interface CategoriesDataSource {
    fun saveCategory(category: ProductCategory): Boolean
    fun saveAllCategories(categories: Array<ProductCategory>): Boolean
    fun loadCategories(): List<ProductCategory>
}