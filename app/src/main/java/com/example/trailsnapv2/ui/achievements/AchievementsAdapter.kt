package com.example.trailsnapv2.ui.achievements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.R
import com.example.trailsnapv2.entities.SingularAchievement

class AchievementsAdapter : ListAdapter<SingularAchievement, AchievementsAdapter.AchievementViewHolder>(AchievementDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_achievement, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val achievement = getItem(position)
        holder.bind(achievement)
    }

    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val achievementName: TextView = itemView.findViewById(R.id.achievement_name)
        private val achievementDescription: TextView = itemView.findViewById(R.id.achievement_description)

        fun bind(achievement: SingularAchievement) {
            achievementName.text = achievement.name_achievement
            achievementDescription.text = achievement.description_achievement
        }
    }

    class AchievementDiffCallback : DiffUtil.ItemCallback<SingularAchievement>() {
        override fun areItemsTheSame(oldItem: SingularAchievement, newItem: SingularAchievement): Boolean {
            return oldItem.id_achievement == newItem.id_achievement
        }

        override fun areContentsTheSame(oldItem: SingularAchievement, newItem: SingularAchievement): Boolean {
            return oldItem == newItem
        }
    }
}
