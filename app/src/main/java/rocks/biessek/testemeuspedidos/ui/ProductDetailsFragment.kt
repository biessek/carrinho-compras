package rocks.biessek.testemeuspedidos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_product_details.*
import kotlinx.android.synthetic.main.fragment_product_details.view.*
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
import rocks.biessek.testemeuspedidos.App
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.ui.model.Product
import rocks.biessek.testemeuspedidos.ui.viewmodel.ProductsViewModel
import rocks.biessek.testemeuspedidos.ui.viewmodel.ProductsViewModelFactory

class ProductDetailsFragment : Fragment() {
    val kodein = LateInitKodein()
    private lateinit var productsViewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodein.baseKodein = (activity!!.applicationContext as App).kodein
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configureToolbar()
        configureProductDetailsViewModel()

        loadProduct()
    }

    private fun loadProduct() {
        val productId = arguments?.getString("product_id")?.toLong()
        productsViewModel.loadProduct(productId)
    }

    private fun configureProductDetailsViewModel() {
        val productsInteractors: ProductsInteractors by kodein.instance()

        productsViewModel = ViewModelProviders.of(activity!!, ProductsViewModelFactory(productsInteractors))
                .get(ProductsViewModel::class.java)
        productsViewModel.selectedProduct.observe(activity!!, Observer { product ->
            if (product != null) {
                refreshLayout(product)
            }
            changeContentVisibility(product)
        })
    }

    private fun refreshLayout(product: Product) {
        view?.content?.let {
            Glide.with(context!!)
                    .setDefaultRequestOptions(RequestOptions().apply {
                        centerInside()
                    })
                    .load(product.photo)
                    .into(it.image)
            it.name.text = product.name
            it.value.text = getString(R.string.product_value, product.price)
            it.description.text = product.description
            it.favorite.isChecked = product.favorite
            it.favorite.setOnClickListener { productsViewModel.toggleProductFavoriteStatus(product) }
        }
    }

    private fun changeContentVisibility(product: Product?) {
        view?.let {
            it.details.progressBar.visibility = View.GONE
            if (product == null) {
                it.error_text.visibility = View.VISIBLE
                it.content.visibility = View.INVISIBLE
            } else {
                it.error_text.visibility = View.INVISIBLE
                it.content.visibility = View.VISIBLE
            }

        }
    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(details.toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowHomeEnabled(true)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
