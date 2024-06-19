package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nutriomatic.app.R
import com.nutriomatic.app.data.remote.api.response.Transaction
import com.nutriomatic.app.databinding.ItemTransactionBinding
import com.nutriomatic.app.presentation.helper.util.convertToLocalDateTimeString
import java.util.Locale

class ListTransactionAdapter(
    private val onItemClick: ((Transaction) -> Unit)? = null,
) : PagingDataAdapter<Transaction, ListTransactionAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Transaction) {
            with(binding) {
                tvId.text = item.tscId
                tvCreatedAt.text = convertToLocalDateTimeString(item.createdAt)
                tvStatus.text = item.tscStatus.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                tvStatus.backgroundTintList = tvStatus.context.getColorStateList(
                    when (item.tscStatus.lowercase()) {
                        "accepted", "paid" -> R.color.label_green
                        "declined" -> R.color.label_red
                        else -> R.color.label_yellow
                    }
                )
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