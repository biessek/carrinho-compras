package rocks.biessek.testemeuspedidos.domain.model

interface ProductsRepository {
    fun loadProducts(): List<Product>
    fun loadProducts(category: ProductCategory): List<Product>
    fun markProductAsFavorite(product: Product): Boolean
}