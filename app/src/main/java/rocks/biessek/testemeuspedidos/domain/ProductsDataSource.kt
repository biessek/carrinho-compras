package rocks.biessek.testemeuspedidos.domain

import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

interface ProductsDataSource {
    fun saveProduct(product: Product): Boolean
    fun loadProducts(): List<Product>
    fun loadProductsFromCategory(category: ProductCategory): List<Product>
}