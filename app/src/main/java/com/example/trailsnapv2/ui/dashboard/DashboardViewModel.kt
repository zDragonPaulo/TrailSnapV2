import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.dao.WalkDao
import com.example.trailsnapv2.entities.SingularAchievement
import com.example.trailsnapv2.entities.User
import com.example.trailsnapv2.entities.UserAchievement
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class DashboardViewModel(private val userDao: UserDao, private val walkDao: WalkDao) : ViewModel() {

    private val _userAchievements = MutableLiveData<List<UserAchievement>>()
    val userAchievements: LiveData<List<UserAchievement>> = _userAchievements

    private val _singularAchievements = MutableLiveData<List<SingularAchievement>>()
    val singularAchievements: LiveData<List<SingularAchievement>> = _singularAchievements

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _totalWalks = MutableLiveData<Int>()
    val totalWalks: LiveData<Int> = _totalWalks
    init {
        loadAchievements()
    }

    fun loadUserData(userId: Long) {
        viewModelScope.launch {
            val user = userDao.getUserById(userId).firstOrNull()
            if (user != null) {
                _userData.postValue(user!!)
            }
        }
    }

    private fun loadAchievements() {
        // Fetch achievements data (simulated here)
        _userAchievements.value = listOf()
        _singularAchievements.value = listOf()
    }
    fun loadTotalWalks(userId: Long) {
        viewModelScope.launch {
            val total = walkDao.calculateWalksCompleted(userId)
            _totalWalks.postValue(total)
        }
    }
}
