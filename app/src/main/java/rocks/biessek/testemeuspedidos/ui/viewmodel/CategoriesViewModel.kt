package rocks.biessek.testemeuspedidos.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.experimental.launch
import rocks.biessek.testemeuspedidos.AppIdlingResource
import rocks.biessek.testemeuspedidos.domain.CategoriesInteractors
import rocks.biessek.testemeuspedidos.ui.model.ProductCategory


class CategoriesViewModel(
        private val categoriesInteractors: CategoriesInteractors
) : ViewModel() {
    private var categories = MutableLiveData<List<ProductCategory>>()

    val categoriesList: LiveData<List<ProductCategory>>
        get() = categories

    fun loadCategories() {
        AppIdlingResource.increment()
        launch {
            val loaded = categoriesInteractors.listCategories().map {
                ProductCategory(it.id, it.name)
            }
            categories.postValue(loaded)
            AppIdlingResource.decrement()
        }
    }

}

class CategoriesViewModelFactory(private val categoriesInteractors: CategoriesInteractors
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoriesViewModel(categoriesInteractors) as T
    }

}