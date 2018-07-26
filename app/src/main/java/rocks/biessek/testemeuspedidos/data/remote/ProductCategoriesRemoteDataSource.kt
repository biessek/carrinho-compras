package rocks.biessek.testemeuspedidos.data.remote

import rocks.biessek.testemeuspedidos.domain.CategoriesDataSource
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory
import java.io.IOException

class ProductCategoriesRemoteDataSource(private val serviceApi: ServiceApi) : CategoriesDataSource {
    override fun saveAllCategories(categories: Array<ProductCategory>): Boolean {
        throw UnsupportedOperationException()
    }

    override fun saveCategory(category: ProductCategory): Boolean {
        throw UnsupportedOperationException()
    }

    override fun loadCategories(): List<ProductCategory> {
        try {
            val response = serviceApi.listCategories().execute()
            if (response.isSuccessful) {
                return response.body() ?: emptyList()
            }
        } catch (exception: IOException) {
            return emptyList()
        } catch (exception: RuntimeException) {
            return emptyList()
        }
        return emptyList()
    }

}