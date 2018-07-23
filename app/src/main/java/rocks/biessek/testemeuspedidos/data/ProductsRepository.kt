package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.domain.ProductsDataSource
import rocks.biessek.testemeuspedidos.domain.model.Product
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

class ProductsRepository(private val localProductsDataSource: ProductsDataSource,
                         private val remoteProductsDataSource: ProductsDataSource) : ProductsDataSource {

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

    override fun loadProductsFromCategory(category: ProductCategory): List<Product> =
            localProductsDataSource.loadProductsFromCategory(category)
}