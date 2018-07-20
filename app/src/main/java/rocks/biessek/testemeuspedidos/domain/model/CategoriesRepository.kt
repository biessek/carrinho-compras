package rocks.biessek.testemeuspedidos.domain.model

interface CategoriesRepository {
    fun loadCategories(): List<ProductCategory>
}