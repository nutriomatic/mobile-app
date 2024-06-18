package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nutriomatic.app.data.remote.api.response.Transaction
import com.nutriomatic.app.databinding.ItemTransactionBinding

class ListTransactionAdapter(
    private val onItemClick: ((Transaction) -> Unit)? = null,
) : PagingDataAdapter<Transaction, ListTransactionAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction) {
            with(binding) {
                tvId.text = item.tscId
                tvStatus.text = item.tscStatus
                tvCreatedAt.text = item.createdAt
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(
                oldItem: Transaction,
                newItem: Transaction,
            ): Boolean {
                return oldItem.tscId == newItem.tscId
            }

            override fun areContentsTheSame(
                oldItem: Transaction,
                newItem: Transaction,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}