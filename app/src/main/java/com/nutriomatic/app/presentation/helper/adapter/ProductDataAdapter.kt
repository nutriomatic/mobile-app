package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.ItemProductBinding
import com.nutriomatic.app.presentation.helper.util.formatCurrency

class ProductDataAdapter(
    private val showEdit: Boolean = false,
    private val onIconClick: ((ProductsItem) -> Unit)? = null,
    private val onItemClick: ((ProductsItem) -> Unit)? = null,
) : PagingDataAdapter<ProductsItem, ProductDataAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            Glide.with(itemView.context)
                .load(item.productPicture)
                .into(binding.ivPhoto)

            with(binding) {
                tvName.text = item.productName
                tvPrice.text = formatCurrency(item.productPrice)
                ivEdit.visibility = if (showEdit) View.VISIBLE else View.GONE
                tvStatus.visibility = if (showEdit) View.VISIBLE else View.GONE
                when (item.productIsshow) {
                    0 -> {
                        tvStatus.text = "Not Advertise"
                        tvStatus.backgroundTintList =
                            tvStatus.context.getColorStateList(R.color.label_blue)
                    }

                    1 -> {
                        tvStatus.text = "Advertise"
                        tvStatus.backgroundTintList =
                            tvStatus.context.getColorStateList(R.color.label_green)
                    }

                    2 -> {
                        tvStatus.text = "Process Advertise"
                        tvStatus.backgroundTintList =
                            tvStatus.context.getColorStateList(R.color.label_yellow)
                    }

                    3 -> {
                        tvStatus.text = "Decline Advertise"
                        tvStatus.backgroundTintList =
                            tvStatus.context.getColorStateList(R.color.label_red)
                    }

                    else -> {
                        tvStatus.text = "Error Advertise"
                        tvStatus.backgroundTintList =
                            tvStatus.context.getColorStateList(R.color.label_red)
                    }

                }
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
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

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