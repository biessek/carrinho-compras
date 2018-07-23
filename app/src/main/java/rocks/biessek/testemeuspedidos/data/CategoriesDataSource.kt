package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.data.model.ProductCategory

interface CategoriesDataSource {
    fun saveCategory(category: ProductCategory): Boolean
    fun loadCategories(): List<ProductCategory>
}