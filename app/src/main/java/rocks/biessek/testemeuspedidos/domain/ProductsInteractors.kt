package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.usecases.DetailProductUseCase
import rocks.biessek.testemeuspedidos.domain.usecases.FavoriteProductUseCase
import rocks.biessek.testemeuspedidos.domain.usecases.FilterProductsUseCase
import rocks.biessek.testemeuspedidos.domain.usecases.ListProductsUseCase

class ProductsInteractors(private val productsDataSource: ProductsDataSource) :
        ListProductsUseCase,
        FilterProductsUseCase,
        FavoriteProductUseCase,
        DetailProductUseCase {

    override suspend fun toggleProductFavorite(product: Product): Boolean {
        product.favorite = !product.favorite
        return productsDataSource.saveProduct(product)
    }

    override suspend fun listProductsFromCategory(categoryId: Long): List<Product> =
            productsDataSource.loadProductsFromCategory(categoryId)

    override suspend fun listAllProducts(): List<Product> = productsDataSource.loadProducts()

    override suspend fun loadProductById(id: Long): Product? =
            productsDataSource.loadProductById(id)


}