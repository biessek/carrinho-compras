package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.data.model.ProductCategory

interface CategoriesDataSource {
    fun loadCategories(): List<ProductCategory>
}