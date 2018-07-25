package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory
import rocks.biessek.testemeuspedidos.domain.usecases.FavoriteProductUseCase
import rocks.biessek.testemeuspedidos.domain.usecases.FilterProductsUseCase
import rocks.biessek.testemeuspedidos.domain.usecases.ListProductsUseCase

class ProductsInteractors(private val productsDataSource: ProductsDataSource) :
        ListProductsUseCase,
        FilterProductsUseCase,
        FavoriteProductUseCase {
    override suspend fun toggleProductFavorite(product: Product): Boolean {
        product.favorite = !product.favorite
        return productsDataSource.saveProduct(product)
    }


    override suspend fun listProductsFromCategory(categoryId: Long): List<Product> =
            productsDataSource.loadProductsFromCategory(categoryId)

    override suspend fun listAllProducts(): List<Product> = productsDataSource.loadProducts()
}