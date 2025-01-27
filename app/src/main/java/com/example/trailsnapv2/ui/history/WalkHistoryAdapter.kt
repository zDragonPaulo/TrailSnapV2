package com.example.trailsnapv2.ui.history

import android.graphics.drawable.Drawable
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
        private val photoPath: ImageView = itemView.findViewById(R.id.item_photo_path)  // Corrected

        fun bind(item: Walk) {
            binding.apply {
                walkName.text = item.walk_name
                distance.text = item.distance.toString()
                startTime.text = item.start_time.toString()
                endTime.text = item.end_time.toString()

                val uri = Uri.parse(item.photo_path)

                try {
                    // Usa o ContentResolver para abrir o input stream da URI
                    val inputStream = itemView.context.contentResolver.openInputStream(uri)
                    val drawable = Drawable.createFromStream(inputStream, uri.toString())
                    photoPath.setImageDrawable(drawable)
                    inputStream?.close()  // Fecha o stream após o uso
                } catch (e: Exception) {
                    e.printStackTrace()
                    // Define uma imagem de placeholder em caso de erro
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
