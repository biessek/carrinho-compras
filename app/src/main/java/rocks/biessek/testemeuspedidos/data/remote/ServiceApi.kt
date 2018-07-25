package rocks.biessek.testemeuspedidos.data.remote

import retrofit2.Call
import retrofit2.http.GET
import rocks.biessek.testemeuspedidos.ui.model.Product
import rocks.biessek.testemeuspedidos.ui.model.ProductCategory

interface ServiceApi {
    @GET("b95b75cfddc6b1cb601d7f806859e1dc/raw/dc973df65664f6997eeba30158d838c4b716204c/products.json")
    fun listProducts(): Call<List<Product>>

    @GET("e84d0d969613fd0ef8f9fd08546f7155/raw/a0611f7e765fa2b745ad9a897296e082a3987f61/categories.json")
    fun listCategories(): Call<List<ProductCategory>>

}