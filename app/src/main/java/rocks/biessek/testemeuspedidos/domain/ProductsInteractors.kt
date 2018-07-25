package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory
import rocks.biessek.testemeuspedidos.domain.usecases.FilterProductsUseCase
import rocks.biessek.testemeuspedidos.domain.usecases.ListProductsUseCase

class ProductsInteractors(private val productsDataSource: ProductsDataSource) :
        ListProductsUseCase,
        FilterProductsUseCase {
    override suspend fun listProductsFromCategory(category: ProductCategory): List<Product> =
            productsDataSource.loadProductsFromCategory(category)

    override suspend fun listAllProducts(): List<Product> = productsDataSource.loadProducts()
}