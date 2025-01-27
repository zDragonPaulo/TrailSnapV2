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

class WalkHistoryAdapter(private val context: Context) :
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

        private val walkName: TextView = itemView.findViewById(R.id.item_walk_name)
        private val distance: TextView = itemView.findViewById(R.id.item_distance)
        private val timeOfWalTotal: TextView = itemView.findViewById(R.id.item_start_time)
        private val photoPath: ImageView = itemView.findViewById(R.id.item_photo_path)

        fun bind(item: Walk) {
            binding.apply {
                walkName.text = item.walk_name
                distance.text = context.getString(R.string.dist_ncia_2f_km, item.distance)
                val startTime = item.start_time
                val endTime = item.end_time
                val elapsedTime = (endTime - startTime) / 1000
                timeOfWalTotal.text = context.getString(R.string.total_time, formatTime(elapsedTime))

                val photoFile = File(item.photo_path ?: "")

                if (photoFile.exists()) {
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                    photoPath.setImageBitmap(bitmap)
                } else {
                    // Define uma imagem placeholder se o arquivo não for encontrado
                    photoPath.setImageResource(R.drawable.ic_user_placeholder)
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
