package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.usecases.ListProductsUseCase

class ProductsInteractors(private val productsDataSource: ProductsDataSource) : ListProductsUseCase {
    override suspend fun listAllProducts(): List<Product> = productsDataSource.loadProducts()
}