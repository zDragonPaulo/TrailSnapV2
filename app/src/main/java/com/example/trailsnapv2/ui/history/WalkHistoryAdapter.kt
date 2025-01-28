package com.example.trailsnapv2.ui.history

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.R
import com.example.trailsnapv2.databinding.ItemWalkHistoryBinding
import com.example.trailsnapv2.entities.Walk
import java.io.File

/**
 * An adapter for displaying walk history items in a [RecyclerView].
 *
 * This adapter is used to bind a list of [Walk] objects to a [RecyclerView] where each item
 * represents a walk that the user has completed. The adapter handles displaying the walk's name,
 * distance, duration, and a photo associated with the walk.
 *
 * @param context The context used to access resources and inflate views.
 * @param onItemClick A lambda function to handle click events on individual walk items.
 */
class WalkHistoryAdapter(private val context: Context, private val onItemClick: (Long) -> Unit) :
    ListAdapter<Walk, WalkHistoryAdapter.WalkHistoryViewHolder>(WalkHistoryDiffCallback()) {

    /**
     * Called when a new [WalkHistoryViewHolder] needs to be created.
     *
     * This method inflates the view for a single item in the [RecyclerView] and returns a new
     * [WalkHistoryViewHolder] that holds a reference to the item view.
     *
     * @param parent The parent view group into which the new view will be added.
     * @param viewType The view type of the new view.
     * @return A new [WalkHistoryViewHolder] instance.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalkHistoryViewHolder {
        val binding =
            ItemWalkHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalkHistoryViewHolder(binding)
    }

    /**
     * Binds the data from a [Walk] item to the [WalkHistoryViewHolder].
     *
     * This method is called for each item in the list. It binds the walk data (e.g., name, distance,
     * time, and photo) to the views in the [WalkHistoryViewHolder].
     *
     * @param holder The [WalkHistoryViewHolder] that will hold the data for the current item.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: WalkHistoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    /**
     * ViewHolder for each individual walk item in the list.
     *
     * This inner class binds the data for a single [Walk] item to its corresponding views.
     * It is used to represent each item in the [RecyclerView].
     */
    inner class WalkHistoryViewHolder(private val binding: ItemWalkHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a [Walk] object to the views in this [WalkHistoryViewHolder].
         *
         * This method sets the text for the walk's name, distance, and duration, and sets the
         * walk's photo if available.
         *
         * @param item The [Walk] item to bind to the views.
         */
        fun bind(item: Walk) {
            binding.apply {
                itemWalkName.text = item.walk_name
                itemWalkDistance.text = context.getString(R.string.dist_ncia_2f_km, item.distance)
                val elapsedTime = (item.end_time - item.start_time) / 1000
                itemWalkTime.text = context.getString(R.string.total_time, formatTime(elapsedTime))

                root.setOnClickListener {
                    onItemClick(item.walk_id)
                }

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

    /**
     * DiffUtil callback for calculating the difference between two [Walk] lists.
     *
     * This callback is used by [ListAdapter] to efficiently update the list when data changes.
     */
    class WalkHistoryDiffCallback : DiffUtil.ItemCallback<Walk>() {

        /**
         * Compares the items to check if they are the same.
         *
         * @param oldItem The old item to compare.
         * @param newItem The new item to compare.
         * @return True if the items represent the same walk, false otherwise.
         */
        override fun areItemsTheSame(oldItem: Walk, newItem: Walk): Boolean {
            return oldItem.walk_id == newItem.walk_id
        }

        /**
         * Compares the contents of the items to check if they are the same.
         *
         * @param oldItem The old item to compare.
         * @param newItem The new item to compare.
         * @return True if the contents of the items are the same, false otherwise.
         */
        override fun areContentsTheSame(oldItem: Walk, newItem: Walk): Boolean {
            return oldItem == newItem
        }
    }

    /**
     * Formats the time in seconds into a human-readable format (e.g., "1 h 20 min").
     *
     * This method converts the time in seconds into hours, minutes, and seconds, returning the
     * formatted string.
     *
     * @param seconds The time in seconds to format.
     * @return A formatted string representing the time.
     */
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
