package rocks.biessek.testemeuspedidos.domain.usecases

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

interface ListProductsUseCase {
    fun listAllProducts(): List<Product>
}

interface FilterProductsUseCase {
    fun listProductsFromCategory(category: ProductCategory): List<Product>
}

interface FavoriteProductUseCase {
    fun favoriteProduct(product: Product): Boolean
}