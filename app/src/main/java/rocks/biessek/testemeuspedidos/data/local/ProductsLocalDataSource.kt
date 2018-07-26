package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.domain.ProductsDataSource
import rocks.biessek.testemeuspedidos.domain.model.Product

class ProductsLocalDataSource(private val productsDao: ProductsDao) : ProductsDataSource {
    override fun saveAllProducts(products: Array<Product>): Boolean {
        val ids = productsDao.saveAllProducts(products.map { toDataProduct(it) }.toTypedArray())
        return ids.size == products.size
    }

    override fun loadProductById(id: Long): Product? = productsDao.getProductById(id)

    override fun saveProduct(product: Product): Boolean = productsDao.saveProduct(toDataProduct(product)) > 0

    override fun loadProducts(): List<Product> = productsDao.getAllProducts()

    override fun loadProductsFromCategory(categoryId: Long): List<Product> = productsDao.getProductsFromCategory(categoryId)

    private fun toDataProduct(product: Product): rocks.biessek.testemeuspedidos.data.model.Product {
        return mapToDataProduct(product)
    }

    private fun mapToDataProduct(product: Product): rocks.biessek.testemeuspedidos.data.model.Product {
        return rocks.biessek.testemeuspedidos.data.model.Product(
                product.id,
                product.name,
                product.description,
                product.photo,
                product.price,
                product.categoryId,
                product.favorite
        )
    }

}