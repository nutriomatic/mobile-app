package com.nutriomatic.app.presentation.helper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nutriomatic.app.data.fake.model.NutritionScan
import com.nutriomatic.app.databinding.ItemScanHistoryBinding

class ListScanHistoryAdapter(
    private val nutritionScans: List<NutritionScan>,
    private val onItemClick: ((NutritionScan) -> Unit)? = null,
) : ListAdapter<NutritionScan, ListScanHistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    inner class ViewHolder(private val binding: ItemScanHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NutritionScan) {
            Glide.with(itemView.context)
                .load(item.photoUrl)
                .into(binding.ivScanPhoto)

            with(binding) {
                tvScanName.text = "${item.name} - ${item.grade}"
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
        val item = nutritionScans[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return nutritionScans.size
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NutritionScan>() {
            override fun areItemsTheSame(oldItem: NutritionScan, newItem: NutritionScan): Boolean {
                return oldItem.id == newItem.id
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