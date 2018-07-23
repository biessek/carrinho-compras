package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

interface ProductsDataSource {
    fun saveProduct(product: Product): Boolean
    fun loadProducts(): List<Product>
    fun loadProductsFromCategory(category: ProductCategory): List<Product>
}