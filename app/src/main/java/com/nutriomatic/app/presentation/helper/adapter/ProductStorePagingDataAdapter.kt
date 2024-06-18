package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.ItemProductBinding

class ProductStorePagingDataAdapter(
    private val showEdit: Boolean = false,
    private val onIconClick: ((ProductsItem) -> Unit)? = null,
    private val onItemClick: ((ProductsItem) -> Unit)? = null,
) : PagingDataAdapter<ProductsItem, ProductStorePagingDataAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            Glide.with(itemView.context)
                .load(item.productPicture)
                .into(binding.ivPhoto)

            with(binding) {
                tvName.text = item.productName
                when (item.productIsshow) {
                    0 -> tvPrice.text = "Not Advertise"

                    1 -> tvPrice.text = "Advertise"

                    2 -> tvPrice.text = "Process Advertise"

                    3 -> tvPrice.text = "Decline Advertise"

                    else -> tvPrice.text = "Error Advertise"

                }

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//
//    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductsItem>() {
            override fun areItemsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ProductsItem, newItem: ProductsItem): Boolean {
                return oldItem.productId == newItem.productId
            }
        }
    }
}