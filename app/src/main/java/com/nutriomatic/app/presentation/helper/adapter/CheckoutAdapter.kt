package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.remote.api.response.ProductsItem
import com.nutriomatic.app.databinding.ItemProductPaymentBinding
import com.nutriomatic.app.presentation.helper.util.convertToLocalDateTimeString
import com.nutriomatic.app.presentation.helper.util.formatCurrency

class CheckoutAdapter(
    private val listProduct: List<ProductsItem>,
) :
    RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemProductPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProductsItem) {
            Glide.with(itemView.context)
                .load(item.productPicture)
                .into(binding.imgPhoto)

            with(binding) {
                tvItemTitle.text = item.productName
                tvPrice.text = formatCurrency(item.productPrice)
                tvItemPublishedDate.text = convertToLocalDateTimeString(item.updatedAt)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemProductPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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