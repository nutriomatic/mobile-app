package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nutriomatic.app.data.fake.model.Transaction
import com.nutriomatic.app.databinding.ItemTransactionBinding

class ListTransactionAdapter(
    private val transactions: List<Transaction>,
    private val onItemClick: ((Transaction) -> Unit)? = null,
) : ListAdapter<Transaction, ListTransactionAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction) {
            with(binding) {
                tvId.text = item.id
                tvStatus.text = item.status
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
        val item = transactions[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Transaction>() {
            override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
                return oldItem.id == newItem.id
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