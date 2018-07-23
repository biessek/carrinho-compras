package rocks.biessek.testemeuspedidos.data

import rocks.biessek.testemeuspedidos.data.model.ProductCategory

class ProductCategoriesRepository(private val localCategoriesDataSource: CategoriesDataSource,
                                  private val remoteCategoriesDataSource: CategoriesDataSource) : CategoriesDataSource {
    override fun saveCategory(category: ProductCategory): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadCategories(): List<ProductCategory> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}