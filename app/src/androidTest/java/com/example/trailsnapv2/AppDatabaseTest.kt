import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trailsnapv2.AppDatabase
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    @Test
    fun writeUserAndReadInList() {
        val user = User(0, "Osama B.", "I like planes", "2001-09-11", 0.0, 0, "2025-01-23", "file:///path/to/photo.jpg")
        userDao.insert(user)
        val users = userDao.getAll()
        assertEquals(users[0].username, user.username)
    }
}