package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.data.ProductsDataSource
import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

class ProductsLocalDataSource(private val productsDao: ProductsDao) : ProductsDataSource {

    override fun saveProduct(product: Product): Boolean = productsDao.saveProduct(product) > 0

    override fun loadProducts(): List<Product> = productsDao.getAllProducts()

    override fun loadProductsFromCategory(category: ProductCategory): List<Product> = productsDao.getProductsFromCategory(category.id)
}