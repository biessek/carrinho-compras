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
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.categories_list.*
import kotlinx.android.synthetic.main.categories_list.view.*
import kotlinx.android.synthetic.main.fragment_products.*
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
    }

    private fun configureProductsViewModel() {
        val productsInteractors: ProductsInteractors by kodein.instance()

        productsViewModel = ViewModelProviders.of(this, ProductsViewModelFactory(productsInteractors))
                .get(ProductsViewModel::class.java)
        productsViewModel.productsList.observe(this, Observer { products ->
            productsAdapter.submitList(products)
            changeContentVisibility(products)
        })

        productsViewModel.selectedCategoryId.observe(this, Observer { selectedCategoryId ->
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

    override fun onCategorySelected(categoryId: Long) {
        productsViewModel.filterFromCategoryId(categoryId)
        toggleCategoriesMenu()
    }

    private fun configureCategoriesDrawer() {
        drawer_menu.setOnClickListener {
            toggleCategoriesMenu()
        }
    }

    private fun toggleCategoriesMenu() {
        if (drawer_layout.isDrawerOpen(Gravity.END)) {
            drawer_layout.closeDrawer(Gravity.END)
        } else {
            drawer_layout.openDrawer(Gravity.END)
        }
    }

    private fun changeCategoriesContentVisibility(categories: List<ProductCategory>) {
        categoriesAdapter.submitList(categories)
        categories_layout.progressBar.visibility = View.GONE
        categories_list.visibility = View.VISIBLE
    }

    private fun changeContentVisibility(products: List<Product>) {
        main_content.progressBar.visibility = View.GONE
        if (products.isEmpty()) {
            empty_text.visibility = View.VISIBLE
            products_list.visibility = View.INVISIBLE
        } else {
            empty_text.visibility = View.INVISIBLE
            products_list.visibility = View.VISIBLE
        }
    }

    private fun configureProductsList() {
        productsAdapter = ProductsAdapter(this)
        products_list.adapter = productsAdapter
        products_list.setHasFixedSize(true)
        products_list.layoutManager = LinearLayoutManager(context)
        products_list.itemAnimator = DefaultItemAnimator()
    }

    private fun configureCategoriesList() {
        categoriesAdapter = CategoriesAdapter(context!!, this)
        categories_list.adapter = categoriesAdapter
        categories_list.setHasFixedSize(true)
        categories_list.layoutManager = LinearLayoutManager(context)
        categories_list.itemAnimator = DefaultItemAnimator()
    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

}
