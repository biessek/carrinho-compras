package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.domain.ProductsDataSource
import rocks.biessek.testemeuspedidos.domain.model.Product

class ProductsRepository(private val localProductsDataSource: ProductsDataSource,
                         private val remoteProductsDataSource: ProductsDataSource) : ProductsDataSource {

    override fun saveProduct(product: Product): Boolean =
            localProductsDataSource.saveProduct(product)

    override fun saveAllProducts(products: Array<Product>): Boolean =
            localProductsDataSource.saveAllProducts(products)

    override fun loadProductById(id: Long): Product? = localProductsDataSource.loadProductById(id)

    override fun loadProducts(): List<Product> {
        val localResult = localProductsDataSource.loadProducts()
        if (localResult.isNotEmpty()) {
            return localResult
        }
        val remoteResult = remoteProductsDataSource.loadProducts()
        if (remoteResult.isNotEmpty() &&
                localProductsDataSource.saveAllProducts(remoteResult.toTypedArray())) {
            return loadProducts()
        }

        return emptyList()
    }

    override fun loadProductsFromCategory(categoryId: Long): List<Product> =
            localProductsDataSource.loadProductsFromCategory(categoryId)
}