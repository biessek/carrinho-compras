package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.domain.ProductsDataSource
import rocks.biessek.testemeuspedidos.domain.model.Product

class ProductsLocalDataSource(private val productsDao: ProductsDao) : ProductsDataSource {
    override fun loadProductById(id: Long): Product? = productsDao.getProductById(id)

    override fun saveProduct(product: Product): Boolean = productsDao.saveProduct(mapDataProduct(product)) > 0

    override fun loadProducts(): List<Product> = productsDao.getAllProducts()

    override fun loadProductsFromCategory(categoryId: Long): List<Product> = productsDao.getProductsFromCategory(categoryId)

    private fun mapDataProduct(product: Product): rocks.biessek.testemeuspedidos.data.model.Product {
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