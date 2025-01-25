package com.example.trailsnapv2

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.trailsnapv2.dao.UserDao
import com.example.trailsnapv2.entities.User
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Unit test class for testing the AppDatabase and UserDao.
 * This class contains tests to verify the functionality of database operations,
 * such as inserting and retrieving data from the "users" table.
 * The tests run using AndroidJUnit4, which allows testing in an Android environment.
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {

    private lateinit var userDao: UserDao
    private lateinit var db: AppDatabase

    /**
     * Creates an in-memory database before each test.
     * This method is executed before each test method to set up the database and user DAO.
     * The in-memory database is used to ensure tests are isolated and do not persist data between runs.
     */
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userDao = db.userDao()
    }

    /**
     * Closes the database after each test.
     * This method is executed after each test method to clean up the database and release resources.
     * It ensures the database is properly closed after each test to avoid memory leaks.
     */
    @After
    @Throws(IOException::class)
    fun closeDb() {
        if (::db.isInitialized) {
            db.close()
        }
    }

    /**
     * Tests writing a User object to the database and reading it back.
     * This test verifies that inserting a user into the database works as expected,
     * and that the user can be retrieved correctly from the database.
     */
    @Test
    fun writeUserAndReadInList() {
        val user = User(
            user_id = 1,
            username = "testUser",
            password = "password123",
            user_description = "Test description",
            birthday = "2000-01-01",
            total_distance = 100.0,
            time_used = 3600L,
            creation_date = "2025-01-01"
        )

        userDao.insert(user)

        val retrievedUser = userDao.getUserByUsername("testUser")
        assertEquals(user.username, retrievedUser?.username)
    }
}