package com.example.trailsnapv2.ui.history

import android.graphics.BitmapFactory
import android.net.Uri
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

class WalkHistoryAdapter :
    ListAdapter<Walk, WalkHistoryAdapter.WalkHistoryViewHolder>(WalkHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkHistoryViewHolder {
        val binding = ItemWalkHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val startTime: TextView = itemView.findViewById(R.id.item_start_time)
        private val endTime: TextView = itemView.findViewById(R.id.item_end_time)
        private val photoPath: ImageView = itemView.findViewById(R.id.item_photo_path)

        fun bind(item: Walk) {
            binding.apply {
                walkName.text = item.walk_name
                distance.text = "Distância: %.2f km".format(item.distance)
                startTime.text = "Início: ${item.start_time}"
                endTime.text = "Fim: ${item.end_time}"

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
}
