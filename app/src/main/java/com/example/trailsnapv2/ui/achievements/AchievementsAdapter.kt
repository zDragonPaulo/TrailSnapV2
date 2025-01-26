import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trailsnapv2.databinding.ItemAchievementBinding
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement

class AchievementsAdapter : RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    private var userAchievements: List<UserAchievement> = emptyList()
    private var singularAchievements: List<SingularAchievement> = emptyList()

    // This function is called to set the user achievements
    fun setUserAchievements(newUserAchievements: List<UserAchievement>, newSingularAchievements: List<SingularAchievement>) {
        userAchievements = newUserAchievements
        singularAchievements = newSingularAchievements
        notifyDataSetChanged()  // Refreshes the RecyclerView with the new data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = ItemAchievementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        val userAchievement = userAchievements[position]
        val singularAchievement = singularAchievements.find { it.id_achievement == userAchievement.achievement_id }
        holder.bind(userAchievement, singularAchievement)
    }

    override fun getItemCount(): Int = userAchievements.size

    inner class AchievementViewHolder(private val binding: ItemAchievementBinding) : RecyclerView.ViewHolder(binding.root) {
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
