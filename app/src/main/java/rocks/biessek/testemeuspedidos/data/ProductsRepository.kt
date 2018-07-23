package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

class ProductsRepository(private val localProductsDataSource: ProductsDataSource,
                         private val remoteProductsDataSource: ProductsDataSource) : ProductsDataSource {
    override fun saveProduct(product: Product): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadProducts(): List<Product> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadProductsFromCategory(category: ProductCategory): List<Product> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}