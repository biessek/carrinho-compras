package rocks.biessek.testemeuspedidos.data.remote

import rocks.biessek.testemeuspedidos.domain.ProductsDataSource
import rocks.biessek.testemeuspedidos.domain.model.Product
import java.io.IOException

class ProductsRemoteDataSource(private val serviceApi: ServiceApi) : ProductsDataSource {
    override fun saveProduct(product: Product): Boolean {
        throw UnsupportedOperationException()
    }

    override fun loadProducts(): List<Product> {
        try {
            val response = serviceApi.listProducts().execute()
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

    override fun loadProductsFromCategory(categoryId: Long): List<Product> {
        throw UnsupportedOperationException()
    }
}