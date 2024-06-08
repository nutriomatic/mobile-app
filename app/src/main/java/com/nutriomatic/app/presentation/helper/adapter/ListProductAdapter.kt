package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.fake.model.Product
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.ItemProductBinding

class ListProductAdapter(
    private val listProduct: List<ProductsItem>,
    private val showEdit: Boolean = false,
    private val onIconClick: ((ProductsItem) -> Unit)? = null,
    private val onItemClick: ((ProductsItem) -> Unit)? = null,
) :
    RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            Glide.with(itemView.context)
                .load(item.productPicture)
                .into(binding.ivPhoto)

            with(binding) {
                tvName.text = item.productName
                tvPrice.text = item.productPrice.toString()
                ivEdit.visibility = if (showEdit) View.VISIBLE else View.GONE
                ivEdit.setOnClickListener {
                    onIconClick?.invoke(item)
                }
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listProduct[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}