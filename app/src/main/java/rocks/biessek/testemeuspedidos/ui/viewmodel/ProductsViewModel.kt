package rocks.biessek.testemeuspedidos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.experimental.async
import rocks.biessek.testemeuspedidos.AppIdlingResource
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.ui.model.Product


class ProductsViewModel(
        private val productsInteractors: ProductsInteractors
) : ViewModel() {
    private var products = MutableLiveData<List<Product>>()

    val productsList: LiveData<List<Product>>
        get() {
            if (products.value == null) {
                listProducts()
            }
            return products
        }

    private fun listProducts() {
        AppIdlingResource.increment()
        async {
            val loaded = productsInteractors.listAllProducts().map {
                Product(it.id, it.name, it.description, it.photo, it.price, it.categoryId, it.favorite)
            }
            products.postValue(loaded)
            AppIdlingResource.decrement()
        }
    }
}

class ProductsViewModelFactory(private val productsInteractors: ProductsInteractors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductsViewModel(productsInteractors) as T
    }

}