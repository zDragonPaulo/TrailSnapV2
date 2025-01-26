package com.example.trailsnapv2.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.Walk

class WalkHistoryAdapter : ListAdapter<Walk, WalkHistoryAdapter.WalkHistoryViewHolder>(WalkHistoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkHistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_walk_history, parent, false)
        return WalkHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: WalkHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class WalkHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val walkName: TextView = itemView.findViewById(R.id.item_walk_name)
        private val distance: TextView = itemView.findViewById(R.id.item_distance)
        private val startTime: TextView = itemView.findViewById(R.id.item_start_time)
        private val endTime: TextView = itemView.findViewById(R.id.item_end_time)

        fun bind(item: Walk) {
            walkName.text = item.walk_name
            distance.text = item.distance.toString()
            startTime.text = item.start_time.toString()
            endTime.text = item.end_time.toString()
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