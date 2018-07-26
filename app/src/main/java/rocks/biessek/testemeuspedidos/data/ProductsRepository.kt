package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.domain.ProductsDataSource
import rocks.biessek.testemeuspedidos.domain.model.Product

class ProductsRepository(private val localProductsDataSource: ProductsDataSource,
                         private val remoteProductsDataSource: ProductsDataSource) : ProductsDataSource {
    override fun loadProductById(id: Long): Product? = localProductsDataSource.loadProductById(id)

    override fun saveProduct(product: Product): Boolean =
            localProductsDataSource.saveProduct(product)

    override fun loadProducts(): List<Product> {
        val localResult = localProductsDataSource.loadProducts()

        if (localResult.isNotEmpty()) {
            return localResult
        }
        val remoteResult = remoteProductsDataSource.loadProducts()
        remoteResult.forEach { saveProduct(it) }
        return remoteResult
    }

    override fun loadProductsFromCategory(categoryId: Long): List<Product> {
        return localProductsDataSource.loadProductsFromCategory(categoryId)
    }
}