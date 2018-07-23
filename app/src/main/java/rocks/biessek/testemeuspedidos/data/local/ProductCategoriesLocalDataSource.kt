package rocks.biessek.testemeuspedidos.data.local

import rocks.biessek.testemeuspedidos.data.CategoriesDataSource
import rocks.biessek.testemeuspedidos.data.ProductsDataSource
import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

class ProductCategoriesLocalDataSource(private val categoriesDao: ProductCategoriesDao) : CategoriesDataSource {
    override fun loadCategories(): List<ProductCategory> {
        return ArrayList()
    }
}