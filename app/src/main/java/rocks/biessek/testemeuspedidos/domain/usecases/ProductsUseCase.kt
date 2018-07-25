package rocks.biessek.testemeuspedidos.domain.usecases

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

interface ListProductsUseCase {
    suspend fun listAllProducts(): List<Product>
}

interface FilterProductsUseCase {
    suspend fun listProductsFromCategory(categoryId: Long): List<Product>
}

interface FavoriteProductUseCase {
    suspend fun toggleProductFavorite(product: Product): Boolean
}