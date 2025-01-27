import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.UserAchievement

class DashboardViewModel : ViewModel() {

    private val _userAchievements = MutableLiveData<List<UserAchievement>>()
    val userAchievements: LiveData<List<UserAchievement>> = _userAchievements

    private val _singularAchievements = MutableLiveData<List<SingularAchievement>>()
    val singularAchievements: LiveData<List<SingularAchievement>> = _singularAchievements

    // Simulated data loading
    init {
        loadAchievements()
    }

    private fun loadAchievements() {
        // Fetch data from your repository or database
        // Example: Replace with actual repository calls
        _userAchievements.value = listOf() // Load UserAchievement data here
        _singularAchievements.value = listOf() // Load SingularAchievement data here
    }
}
