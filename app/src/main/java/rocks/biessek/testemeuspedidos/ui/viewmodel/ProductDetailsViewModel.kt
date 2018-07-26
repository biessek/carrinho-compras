package rocks.biessek.testemeuspedidos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.experimental.launch
import rocks.biessek.testemeuspedidos.AppIdlingResource
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.ui.model.Product


class ProductDetailsViewModel(
        private val productsInteractors: ProductsInteractors
) : ViewModel() {
    private var product = MutableLiveData<Product>()
    val selectedProduct: LiveData<Product>
        get() {
            return product
        }

    fun loadProduct(id: Long?) {
        AppIdlingResource.increment()
        launch {
            if (id == null) {
                product.postValue(null)
            } else {
                productsInteractors.loadProductById(id)?.let {
                    product.postValue(Product(
                            it.id,
                            it.name,
                            it.description,
                            it.photo,
                            it.price,
                            it.categoryId,
                            it.favorite
                    ))
                }
            }

            AppIdlingResource.decrement()
        }
    }

    fun toggleProductFavoriteStatus(product: Product) {
        launch {
            productsInteractors.toggleProductFavorite(product)
            productsInteractors.loadProductById(product.id!!)
        }
    }

}

class ProductDetailsViewModelFactory(private val productsInteractors: ProductsInteractors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductDetailsViewModel(productsInteractors) as T
    }

}