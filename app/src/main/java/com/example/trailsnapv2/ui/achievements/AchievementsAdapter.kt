package com.example.trailsnapv2.ui.achievements

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.databinding.ItemAchievementBinding
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement

/**
 * RecyclerView Adapter for displaying user achievements in a list.
 * This adapter binds a list of [UserAchievement] objects to their corresponding
 * [SingularAchievement] details in the RecyclerView.
 */
class AchievementsAdapter : RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    private var userAchievements: List<UserAchievement> = emptyList()
    private var singularAchievements: List<SingularAchievement> = emptyList()

    /**
     * Sets the data for both user achievements and singular achievements.
     * This method updates the lists and notifies the adapter to refresh the RecyclerView.
     *
     * @param newUserAchievements The updated list of UserAchievement objects.
     * @param newSingularAchievements The updated list of SingularAchievement objects.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun setUserAchievements(newUserAchievements: List<UserAchievement>, newSingularAchievements: List<SingularAchievement>) {
        userAchievements = newUserAchievements
        singularAchievements = newSingularAchievements
        notifyDataSetChanged()
    }

    /**
     * Creates a new ViewHolder that will hold the view for each item in the RecyclerView.
     *
     * @param parent The parent ViewGroup that the ViewHolder's view will be attached to.
     * @param viewType The type of view to create (for different item layouts).
     * @return A new instance of AchievementViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    /**
     * Binds the data from a UserAchievement and its corresponding SingularAchievement to the ViewHolder.
     * This method is called for each item in the RecyclerView.
     *
     * @param holder The ViewHolder to bind the data to.
     * @param position The position of the item in the list.
     */
    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val userAchievement = userAchievements[position]
        val singularAchievement = singularAchievements.find { it.id_achievement == userAchievement.achievement_id }
        holder.bind(userAchievement, singularAchievement)
    }

    /**
     * Returns the total number of items in the RecyclerView.
     *
     * @return The number of UserAchievement items to display.
     */
    override fun getItemCount(): Int = userAchievements.size

    /**
     * ViewHolder class for holding the views of each achievement item in the RecyclerView.
     * Binds the achievement data to the item views in the layout.
     */
    inner class AchievementViewHolder(private val binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a UserAchievement and its corresponding SingularAchievement to the views in the layout.
         *
         * @param userAchievement The UserAchievement data to display.
         * @param singularAchievement The SingularAchievement that contains additional details about the achievement.
         */
        fun bind(userAchievement: UserAchievement, singularAchievement: SingularAchievement?) {
            singularAchievement?.let {
                binding.textAchievementName.text = it.name_achievement
                binding.textAchievementDescription.text = it.description_achievement
            }
            binding.progressBarAchievement.progress = userAchievement.progress.toInt()
            binding.textAchievementStatus.text = if (userAchievement.unlocked) "Unlocked" else "In Progress"
        }
    }
}
