package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.remote.api.response.TransactionsItem
import com.nutriomatic.app.databinding.ItemProductPaymentBinding
import com.nutriomatic.app.presentation.helper.util.convertToLocalDateTimeString
import com.nutriomatic.app.presentation.helper.util.formatCurrency

class CheckoutAdapter(
    private val listProduct: List<TransactionsItem>,
) :
    RecyclerView.Adapter<CheckoutAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemProductPaymentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TransactionsItem) {
            Glide.with(itemView.context)
                .load(item.tscBukti)
                .into(binding.imgPhoto)

            with(binding) {
                tvItemTitle.text = item.productId
                tvPrice.text = formatCurrency(item.tscPrice.toDouble())
                tvItemPublishedDate.text = convertToLocalDateTimeString(item.tscStart)

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