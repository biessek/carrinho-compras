package rocks.biessek.testemeuspedidos.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.product_list_item.view.*
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.ui.model.Product

class ProductsAdapter(private val listener: ProductItemListener) : ListAdapter<Product, ProductsViewHolder>(diffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductsViewHolder(inflater.inflate(R.layout.product_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val product = getItem(position)
        holder.itemView.favorite.setOnClickListener { listener.onProductFavoriteClick(product) }
        holder.bind(product)
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
        itemView.favorite.isChecked = product.favorite
    }
}

interface ProductItemListener {
    fun onProductFavoriteClick(product: Product)
}