package com.example.trailsnapv2.ui.history

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.ItemWalkHistoryBinding
import com.example.trailsnapv2.entities.Walk
import java.io.File

class WalkHistoryAdapter(private val context: Context, private val onItemClick: (Long) -> Unit) :
    ListAdapter<Walk, WalkHistoryAdapter.WalkHistoryViewHolder>(WalkHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkHistoryViewHolder {
        val binding =
            ItemWalkHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalkHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalkHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class WalkHistoryViewHolder(private val binding: ItemWalkHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Walk) {
            binding.apply {
                itemWalkName.text = item.walk_name
                itemDistance.text = context.getString(R.string.dist_ncia_2f_km, item.distance)
                val elapsedTime = (item.end_time - item.start_time) / 1000
                itemStartTime.text = context.getString(R.string.total_time, formatTime(elapsedTime))

                // Adiciona o clique no item
                root.setOnClickListener {
                    onItemClick(item.walk_id) // Chama o callback passando o ID da caminhada
                }

                // Configurar imagem
                val photoFile = File(item.photo_path ?: "")
                if (photoFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    itemPhotoPath.setImageBitmap(bitmap)
                } else {
                    itemPhotoPath.setImageResource(R.drawable.ic_user_placeholder)
                }
            }
        }
    }

    class WalkHistoryDiffCallback : DiffUtil.ItemCallback<Walk>() {
        override fun areItemsTheSame(oldItem: Walk, newItem: Walk): Boolean {
            return oldItem.walk_id == newItem.walk_id
        }

        override fun areContentsTheSame(oldItem: Walk, newItem: Walk): Boolean {
            return oldItem == newItem
        }
    }

    private fun formatTime(seconds: Long): String {
        return when {
            seconds >= 3600 -> {
                val hours = seconds / 3600
                val minutes = (seconds % 3600) / 60
                if (minutes > 0) "$hours h $minutes min" else "$hours h"
            }
            seconds >= 60 -> {
                val minutes = seconds / 60
                val remainingSeconds = seconds % 60
                if (remainingSeconds > 0) "$minutes min $remainingSeconds s" else "$minutes min"
            }
            else -> "$seconds s"
        }
    }
}
