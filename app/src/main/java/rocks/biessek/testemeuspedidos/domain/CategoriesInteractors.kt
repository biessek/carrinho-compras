package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.ProductCategory
import rocks.biessek.testemeuspedidos.domain.usecases.ListCategoriesUseCase

class CategoriesInteractors(private val categoriesDataSource: CategoriesDataSource): ListCategoriesUseCase {
    override fun listCategories(): List<ProductCategory> = categoriesDataSource.loadCategories()

}