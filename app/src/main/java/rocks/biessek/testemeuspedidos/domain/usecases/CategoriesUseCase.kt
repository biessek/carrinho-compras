package rocks.biessek.testemeuspedidos.domain.usecases

import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

interface ListCategoriesUseCase {
    suspend fun listCategories(): List<ProductCategory>
}