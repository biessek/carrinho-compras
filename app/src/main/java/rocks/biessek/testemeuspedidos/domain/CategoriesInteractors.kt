package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.ProductCategory
import rocks.biessek.testemeuspedidos.domain.usecases.ListCategoriesUseCase

class CategoriesInteractors(categoriesDataSource: CategoriesDataSource): ListCategoriesUseCase {
    override suspend fun listCategories(): List<ProductCategory> = emptyList()

}