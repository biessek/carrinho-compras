package rocks.biessek.testemeuspedidos.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.experimental.launch
import rocks.biessek.testemeuspedidos.AppIdlingResource
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.ui.model.Product


class ProductsViewModel(
        private val productsInteractors: ProductsInteractors
) : ViewModel() {
    private var products = MutableLiveData<List<Product>>()
    private var product = MutableLiveData<Product>()

    val productsList: LiveData<List<Product>>
        get() = products

    val selectedProduct: LiveData<Product>
        get() = product

    val selectedCategoryId: LiveData<Long>

    init {
        selectedCategoryId = Transformations.map(productsList) { products ->
            return@map mapProductsListSelection(products)
        }
    }

    fun filterFromCategoryId(categoryId: Long) {
        loadProducts(categoryId)
    }

    fun loadProducts(categoryId: Long = 0L) {
        AppIdlingResource.increment()
        launch {
            var loaded = if (categoryId == 0L) {
                productsInteractors.listAllProducts()
            } else {
                productsInteractors.listProductsFromCategory(categoryId)
            }

            loaded = loaded.map {
                Product(it.id, it.name, it.description, it.photo, it.price, it.categoryId, it.favorite)
            }
            products.postValue(loaded)
            AppIdlingResource.decrement()
        }
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
            filterFromCategoryId(selectedCategoryId.value ?: 0L)
        }
    }

    /**
     * Certifica-se que possui uma única categoria selecionada('0' caso contrário) e marca o id no adapter
     * */
    private fun mapProductsListSelection(products: List<Product>): Long {
        val categoryId = products.firstOrNull()?.categoryId ?: 0
        return if (products.firstOrNull { product -> product.categoryId != categoryId } == null) {
            categoryId
        } else {
            0L
        }
    }

}

class ProductsViewModelFactory(private val productsInteractors: ProductsInteractors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductsViewModel(productsInteractors) as T
    }

}