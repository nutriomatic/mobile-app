package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.local.LocalData
import com.nutriomatic.app.data.remote.api.response.NutritionScan
import com.nutriomatic.app.databinding.ItemScanHistoryBinding
import com.nutriomatic.app.presentation.helper.util.convertToLocalDateTimeString

class ListScanHistoryAdapter(
    private val onItemClick: ((NutritionScan) -> Unit)? = null,
) : PagingDataAdapter<NutritionScan, ListScanHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemScanHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NutritionScan) {
            Glide.with(itemView.context)
                .load(item.snPicture)
                .into(binding.ivScanPhoto)

            with(binding) {
                tvScanName.text = item.snProductName
                val gradeResId = LocalData.getGradeLabelByName(item.snGrade)
                ivLabel.setImageResource(gradeResId)
                tvScanCreatedAt.text = convertToLocalDateTimeString(item.createdAt)
            }

            itemView.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemScanHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NutritionScan>() {
            override fun areItemsTheSame(oldItem: NutritionScan, newItem: NutritionScan): Boolean {
                return oldItem.snId == newItem.snId
            }

            override fun areContentsTheSame(
                oldItem: NutritionScan,
                newItem: NutritionScan,
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}