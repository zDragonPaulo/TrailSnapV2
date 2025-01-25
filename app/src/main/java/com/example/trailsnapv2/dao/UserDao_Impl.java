package com.example.trailsnapv2.dao;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.trailsnapv2.entities.User;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;

import kotlin.coroutines.Continuation;
import androidx.room.CoroutinesRoom;

import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `users` (`user_id`, `password`, `username`, `user_description`, `birthday`, `total_distance`, `time_used`, `creation_date`, `profile_picture`) VALUES (nullif(?, 0), ?, ?, ?, ?, ?, ?, ?, ?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getUser_id());
        if (value.getPassword() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getPassword());
        }
        if (value.getUsername() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUsername());
        }
        if (value.getUser_description() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getUser_description());
        }
        if (value.getBirthday() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getBirthday());
        }
        stmt.bindDouble(6, value.getTotal_distance());
        stmt.bindLong(7, value.getTime_used());
        if (value.getCreation_date() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getCreation_date());
        }
        if (value.getProfile_picture() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getProfile_picture());
        }
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `users` WHERE `user_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, User value) {
        stmt.bindLong(1, value.getUser_id());
      }
    };
  }
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
  @Override
  public long insert(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfUser.insertAndReturnId(user);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final User user) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfUser.handle(user);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<User> getAll() {
    final String _sql = "SELECT * FROM users";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfUserDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "user_description");
      final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
      final int _cursorIndexOfTotalDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "total_distance");
      final int _cursorIndexOfTimeUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "time_used");
      final int _cursorIndexOfCreationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creation_date");
      final int _cursorIndexOfProfilePicture = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_picture");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final User _item;
        final long _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
        final String _tmpPassword = _cursor.isNull(_cursorIndexOfPassword) ? null : _cursor.getString(_cursorIndexOfPassword);
        final String _tmpUsername = _cursor.isNull(_cursorIndexOfUsername) ? null : _cursor.getString(_cursorIndexOfUsername);
        final String _tmpUser_description = _cursor.isNull(_cursorIndexOfUserDescription) ? null : _cursor.getString(_cursorIndexOfUserDescription);
        final String _tmpBirthday = _cursor.isNull(_cursorIndexOfBirthday) ? null : _cursor.getString(_cursorIndexOfBirthday);
        final double _tmpTotal_distance = _cursor.getDouble(_cursorIndexOfTotalDistance);
        final long _tmpTime_used = _cursor.getLong(_cursorIndexOfTimeUsed);
        final String _tmpCreation_date = _cursor.isNull(_cursorIndexOfCreationDate) ? null : _cursor.getString(_cursorIndexOfCreationDate);
        final String _tmpProfile_picture = _cursor.isNull(_cursorIndexOfProfilePicture) ? null : _cursor.getString(_cursorIndexOfProfilePicture);
        _item = new User(_tmpUser_id, _tmpPassword, _tmpUsername, _tmpUser_description, _tmpBirthday, _tmpTotal_distance, _tmpTime_used, _tmpCreation_date, _tmpProfile_picture);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Nullable
  @Override
  public User getUserByUsername(@NonNull String username) {
    final String _sql = "SELECT * FROM users WHERE username = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfUserDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "user_description");
      final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
      final int _cursorIndexOfTotalDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "total_distance");
      final int _cursorIndexOfTimeUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "time_used");
      final int _cursorIndexOfCreationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creation_date");
      final int _cursorIndexOfProfilePicture = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_picture");
      final User _result;
      if (_cursor.moveToFirst()) {
        final long _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
        final String _tmpPassword = _cursor.isNull(_cursorIndexOfPassword) ? null : _cursor.getString(_cursorIndexOfPassword);
        final String _tmpUsername = _cursor.isNull(_cursorIndexOfUsername) ? null : _cursor.getString(_cursorIndexOfUsername);
        final String _tmpUser_description = _cursor.isNull(_cursorIndexOfUserDescription) ? null : _cursor.getString(_cursorIndexOfUserDescription);
        final String _tmpBirthday = _cursor.isNull(_cursorIndexOfBirthday) ? null : _cursor.getString(_cursorIndexOfBirthday);
        final double _tmpTotal_distance = _cursor.getDouble(_cursorIndexOfTotalDistance);
        final long _tmpTime_used = _cursor.getLong(_cursorIndexOfTimeUsed);
        final String _tmpCreation_date = _cursor.isNull(_cursorIndexOfCreationDate) ? null : _cursor.getString(_cursorIndexOfCreationDate);
        final String _tmpProfile_picture = _cursor.isNull(_cursorIndexOfProfilePicture) ? null : _cursor.getString(_cursorIndexOfProfilePicture);
        _result = new User(_tmpUser_id, _tmpPassword, _tmpUsername, _tmpUser_description, _tmpBirthday, _tmpTotal_distance, _tmpTime_used, _tmpCreation_date, _tmpProfile_picture);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Nullable
  @Override
  public Object getUserById(final long userId, @NotNull final Continuation<? super User> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<User>() {
      @Override
      public User call() throws Exception {
        final String _sql = "SELECT * FROM users WHERE user_id = ?";
        final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
        int _argIndex = 1;
        _statement.bindLong(_argIndex, userId);
        __db.assertNotSuspendingTransaction();
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
          final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
          final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
          final int _cursorIndexOfUserDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "user_description");
          final int _cursorIndexOfBirthday = CursorUtil.getColumnIndexOrThrow(_cursor, "birthday");
          final int _cursorIndexOfTotalDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "total_distance");
          final int _cursorIndexOfTimeUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "time_used");
          final int _cursorIndexOfCreationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creation_date");
          final int _cursorIndexOfProfilePicture = CursorUtil.getColumnIndexOrThrow(_cursor, "profile_picture");
          final User _result;
          if (_cursor.moveToFirst()) {
            final long _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
            final String _tmpPassword = _cursor.isNull(_cursorIndexOfPassword) ? null : _cursor.getString(_cursorIndexOfPassword);
            final String _tmpUsername = _cursor.isNull(_cursorIndexOfUsername) ? null : _cursor.getString(_cursorIndexOfUsername);
            final String _tmpUser_description = _cursor.isNull(_cursorIndexOfUserDescription) ? null : _cursor.getString(_cursorIndexOfUserDescription);
            final String _tmpBirthday = _cursor.isNull(_cursorIndexOfBirthday) ? null : _cursor.getString(_cursorIndexOfBirthday);
            final double _tmpTotal_distance = _cursor.getDouble(_cursorIndexOfTotalDistance);
            final long _tmpTime_used = _cursor.getLong(_cursorIndexOfTimeUsed);
            final String _tmpCreation_date = _cursor.isNull(_cursorIndexOfCreationDate) ? null : _cursor.getString(_cursorIndexOfCreationDate);
            final String _tmpProfile_picture = _cursor.isNull(_cursorIndexOfProfilePicture) ? null : _cursor.getString(_cursorIndexOfProfilePicture);
            _result = new User(_tmpUser_id, _tmpPassword, _tmpUsername, _tmpUser_description, _tmpBirthday, _tmpTotal_distance, _tmpTime_used, _tmpCreation_date, _tmpProfile_picture);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }
}