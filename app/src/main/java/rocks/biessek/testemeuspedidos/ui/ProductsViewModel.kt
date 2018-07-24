package rocks.biessek.testemeuspedidos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.domain.model.Product


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
        products.value = productsInteractors.listAllProducts()
    }
}

class ProductsViewModelFactory(private val productsInteractors: ProductsInteractors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductsViewModel(productsInteractors) as T
    }

}