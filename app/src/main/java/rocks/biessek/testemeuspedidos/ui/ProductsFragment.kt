package rocks.biessek.testemeuspedidos.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_products.*
import kotlinx.android.synthetic.main.product_list_item.view.*
import org.kodein.di.LateInitKodein
import org.kodein.di.generic.instance
import rocks.biessek.testemeuspedidos.App
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors
import rocks.biessek.testemeuspedidos.ui.model.Product
import rocks.biessek.testemeuspedidos.ui.viewmodel.ProductsViewModel
import rocks.biessek.testemeuspedidos.ui.viewmodel.ProductsViewModelFactory


class ProductsFragment : Fragment() {
    val kodein = LateInitKodein()
    lateinit var productsAdapter: ProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kodein.baseKodein = (activity!!.applicationContext as App).kodein
        val productsInteractors: ProductsInteractors by kodein.instance()

        val model = ViewModelProviders.of(this, ProductsViewModelFactory(productsInteractors))
                .get(ProductsViewModel::class.java)
        model.productsList.observe(this, Observer { products ->
            productsAdapter.submitList(products)
            changeContentVisibility(products)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_products, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        configureToolbar()
        configureProductsList()
    }

    private fun changeContentVisibility(products: List<Product>) {
        progressBar.visibility = View.GONE
        if (products.isEmpty()) {
            empty_text.visibility = View.VISIBLE
            products_list.visibility = View.INVISIBLE
        } else {
            empty_text.visibility = View.INVISIBLE
            products_list.visibility = View.VISIBLE
        }
    }

    private fun configureProductsList() {
        productsAdapter = ProductsAdapter()
        products_list.adapter = productsAdapter
        products_list.setHasFixedSize(true)
        products_list.layoutManager = LinearLayoutManager(context)
        products_list.itemAnimator = DefaultItemAnimator()
    }

    private fun configureToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    class ProductsAdapter : ListAdapter<Product, ProductsViewHolder>(diffCallback) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            return ProductsViewHolder(inflater.inflate(R.layout.product_list_item, parent, false))
        }

        override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
            holder.bind(getItem(position))
        }

        override fun submitList(list: List<Product>?) {
            if (list != null) super.submitList(ArrayList(list))
            else super.submitList(null)
        }

        override fun getItemId(position: Int): Long {
            return getItem(position)?.id ?: 0L
        }

        companion object {
            private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
                override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
                        oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
                        oldItem == newItem
            }
        }
    }

    class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(product: Product) {
            Glide.with(itemView.context)
                    .setDefaultRequestOptions(RequestOptions().apply {
                        centerInside()
                    })
                    .load(product.photo)

                    .into(itemView.image)
            itemView.title.text = product.name
            itemView.price.text = "%.1f".format(product.price)
            if (product.favorite) {
                itemView.favorite_off.visibility = View.INVISIBLE
                itemView.favorite_on.visibility = View.VISIBLE
            } else {
                itemView.favorite_off.visibility = View.VISIBLE
                itemView.favorite_on.visibility = View.INVISIBLE
            }
        }
    }

}
