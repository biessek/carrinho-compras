package rocks.biessek.testemeuspedidos.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_list_item.view.*
import rocks.biessek.testemeuspedidos.R
import rocks.biessek.testemeuspedidos.ui.model.ProductCategory


class CategoriesAdapter(context: Context, private val listener: CategorySelectedListener) : ListAdapter<ProductCategory, CategoryViewHolder>(diffCallback) {
    private var categoryAll: ProductCategory = ProductCategory(0, context.getString(R.string.category_all))
    private var selectedItem = 0L

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryViewHolder(inflater.inflate(R.layout.category_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = getItem(position)
        holder.itemView.setOnClickListener { listener.onCategorySelected(category.id) }
        holder.bind(category, selectedItem == category.id)
    }

    override fun submitList(list: List<ProductCategory>?) {
        val newList = arrayListOf(categoryAll)

        if (list != null) newList.addAll(list)

        super.submitList(newList)
    }

    fun setSelected(categoryId: Long) {
        selectedItem = categoryId
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.id ?: 0L
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ProductCategory>() {
            override fun areItemsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean =
                    oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProductCategory, newItem: ProductCategory): Boolean =
                    oldItem == newItem
        }
    }
}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(category: ProductCategory, selected: Boolean) {
        itemView.text.text = category.name
        itemView.isActivated = selected
        if (selected) {
            itemView.text.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorSelectedCategory))
            itemView.checkbox.visibility = View.VISIBLE
        } else {
            itemView.text.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorPrimary))
            itemView.checkbox.visibility = View.INVISIBLE
        }
    }
}

interface CategorySelectedListener {
    fun onCategorySelected(categoryId: Long)
}