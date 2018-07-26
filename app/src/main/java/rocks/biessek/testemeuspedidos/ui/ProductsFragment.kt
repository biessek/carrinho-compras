package rocks.biessek.testemeuspedidos.ui

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.categories_list.view.*
import kotlinx.android.synthetic.main.fragment_products.view.*
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
import rocks.biessek.testemeuspedidos.App
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.domain.CategoriesInteractors
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.ui.adapter.CategoriesAdapter
import rocks.biessek.testemeuspedidos.ui.adapter.CategorySelectedListener
import rocks.biessek.testemeuspedidos.ui.adapter.ProductItemListener
import rocks.biessek.testemeuspedidos.ui.adapter.ProductsAdapter
import rocks.biessek.testemeuspedidos.ui.model.Product
import rocks.biessek.testemeuspedidos.ui.model.ProductCategory
import rocks.biessek.testemeuspedidos.ui.viewmodel.CategoriesViewModel
import rocks.biessek.testemeuspedidos.ui.viewmodel.CategoriesViewModelFactory
import rocks.biessek.testemeuspedidos.ui.viewmodel.ProductsViewModel
import rocks.biessek.testemeuspedidos.ui.viewmodel.ProductsViewModelFactory


class ProductsFragment : Fragment(), CategorySelectedListener, ProductItemListener {
    val kodein = LateInitKodein()
    lateinit var productsAdapter: ProductsAdapter
    lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var categoryViewModel: CategoriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodein.baseKodein = (activity!!.applicationContext as App).kodein
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_products, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureToolbar()
        configureProductsList()

        configureCategoriesDrawer()
        configureCategoriesList()

        configureProductsViewModel()
        configureCategoriesViewModel()

        productsViewModel.loadProducts()
        categoryViewModel.loadCategories()
    }

    private fun configureProductsViewModel() {
        val productsInteractors: ProductsInteractors by kodein.instance()

        productsViewModel = ViewModelProviders.of(activity!!, ProductsViewModelFactory(productsInteractors))
                .get(ProductsViewModel::class.java)
        productsViewModel.productsList.observe(activity!!, Observer { products ->
            productsAdapter.submitList(products)
            changeContentVisibility(products)
        })

        productsViewModel.selectedCategoryId.observe(activity!!, Observer { selectedCategoryId ->
            categoriesAdapter.setSelected(selectedCategoryId)
        })
    }

    private fun configureCategoriesViewModel() {
        val categoriesInteractors: CategoriesInteractors by kodein.instance()

        categoryViewModel = ViewModelProviders.of(this, CategoriesViewModelFactory(categoriesInteractors))
                .get(CategoriesViewModel::class.java)
        categoryViewModel.categoriesList.observe(this, Observer { categories ->
            categoriesAdapter.submitList(categories)
            changeCategoriesContentVisibility(categories)
        })
    }

    override fun onProductFavoriteClick(product: Product) {
        productsViewModel.toggleProductFavoriteStatus(product)
    }

    override fun onProductClick(product: Product) {
        val action = ProductsFragmentDirections.showProductDetailsAction()
        action.setProductId(product.id?.toString())
        findNavController(view!!).navigate(action)
    }

    override fun onCategorySelected(categoryId: Long) {
        productsViewModel.loadProducts(categoryId)
        toggleCategoriesMenu()
    }

    private fun configureCategoriesDrawer() {
        view?.drawer_menu!!.setOnClickListener {
            toggleCategoriesMenu()
        }
    }

    private fun toggleCategoriesMenu() {
        if (view?.drawer_layout!!.isDrawerOpen(Gravity.END)) {
            view?.drawer_layout!!.closeDrawer(Gravity.END)
        } else {
            view?.drawer_layout!!.openDrawer(Gravity.END)
        }
    }

    private fun changeCategoriesContentVisibility(categories: List<ProductCategory>) {
        categoriesAdapter.submitList(categories)
        view?.let {
            it.categories_layout.categoriesProgressBar.visibility = View.GONE
            it.categories_list.visibility = View.VISIBLE
        }
    }

    private fun changeContentVisibility(products: List<Product>) {
        view?.let {
            it.main_content.progressBar.visibility = View.GONE
            if (products.isEmpty()) {
                it.empty_text.visibility = View.VISIBLE
                it.products_list.visibility = View.INVISIBLE
            } else {
                it.empty_text.visibility = View.INVISIBLE
                it.products_list.visibility = View.VISIBLE
            }
        }
    }

    private fun configureProductsList() {
        productsAdapter = ProductsAdapter(this)
        view?.let {
            it.products_list.adapter = productsAdapter
            it.products_list.setHasFixedSize(true)
            it.products_list.layoutManager = LinearLayoutManager(context)
            it.products_list.itemAnimator = DefaultItemAnimator()
        }
    }

    private fun configureCategoriesList() {
        categoriesAdapter = CategoriesAdapter(context!!, this)
        view?.let {
            it.categories_list.adapter = categoriesAdapter
            it.categories_list.setHasFixedSize(true)
            it.categories_list.layoutManager = LinearLayoutManager(context)
            it.categories_list.itemAnimator = DefaultItemAnimator()
        }
    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(view!!.toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

}
