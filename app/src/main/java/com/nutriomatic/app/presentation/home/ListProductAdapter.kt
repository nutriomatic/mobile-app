package com.nutriomatic.app.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.fake.model.Product
import com.nutriomatic.app.databinding.ItemProductBinding

class ListProductAdapter(
    private val listProduct: List<Product>,
    private val onItemClick: ((Product) -> Unit)? = null,
) :
    RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product) {
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(binding.ivPhoto)

            with(binding) {
                tvName.text = item.name
                tvPrice.text = item.price.toString()
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

    interface ClickListener {
        fun onClick(product: Product)
    }
}