package com.example.trailsnapv2.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.trailsnapv2.entities.SingularAchievement;
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
public final class SingularAchievementDao_Impl implements SingularAchievementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SingularAchievement> __insertionAdapterOfSingularAchievement;

  private final EntityDeletionOrUpdateAdapter<SingularAchievement> __deletionAdapterOfSingularAchievement;

  public SingularAchievementDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSingularAchievement = new EntityInsertionAdapter<SingularAchievement>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `singular_achievements` (`id_achievement`,`achievement_type`,`receiver_id`,`name_achievement`,`description_achievement`,`unlocked`,`progress`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SingularAchievement value) {
        stmt.bindLong(1, value.getId_achievement());
        stmt.bindLong(2, value.getReceiver_id());
        if (value.getName_achievement() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getName_achievement());
        }
        if (value.getDescription_achievement() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDescription_achievement());
        }
        final int _tmp = value.getUnlocked() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        stmt.bindDouble(6, value.getProgress());
      }
    };
    this.__deletionAdapterOfSingularAchievement = new EntityDeletionOrUpdateAdapter<SingularAchievement>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `singular_achievements` WHERE `id_achievement` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SingularAchievement value) {
        stmt.bindLong(1, value.getId_achievement());
      }
    };
  }

  @Override
  public long insert(final SingularAchievement achievement) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfSingularAchievement.insertAndReturnId(achievement);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final SingularAchievement achievement) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfSingularAchievement.handle(achievement);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SingularAchievement> getAll() {
    final String _sql = "SELECT * FROM singular_achievements";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfIdAchievement = CursorUtil.getColumnIndexOrThrow(_cursor, "id_achievement");
      final int _cursorIndexOfAchievementType = CursorUtil.getColumnIndexOrThrow(_cursor, "achievement_type");
      final int _cursorIndexOfReceiverId = CursorUtil.getColumnIndexOrThrow(_cursor, "receiver_id");
      final int _cursorIndexOfNameAchievement = CursorUtil.getColumnIndexOrThrow(_cursor, "name_achievement");
      final int _cursorIndexOfDescriptionAchievement = CursorUtil.getColumnIndexOrThrow(_cursor, "description_achievement");
      final int _cursorIndexOfUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "unlocked");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final List<SingularAchievement> _result = new ArrayList<SingularAchievement>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SingularAchievement _item;
        final long _tmpId_achievement;
        _tmpId_achievement = _cursor.getLong(_cursorIndexOfIdAchievement);
        final String _tmpAchievement_type;
        if (_cursor.isNull(_cursorIndexOfAchievementType)) {
          _tmpAchievement_type = null;
        } else {
          _tmpAchievement_type = _cursor.getString(_cursorIndexOfAchievementType);
        }
        final long _tmpReceiver_id;
        _tmpReceiver_id = _cursor.getLong(_cursorIndexOfReceiverId);
        final String _tmpName_achievement;
        if (_cursor.isNull(_cursorIndexOfNameAchievement)) {
          _tmpName_achievement = null;
        } else {
          _tmpName_achievement = _cursor.getString(_cursorIndexOfNameAchievement);
        }
        final String _tmpDescription_achievement;
        if (_cursor.isNull(_cursorIndexOfDescriptionAchievement)) {
          _tmpDescription_achievement = null;
        } else {
          _tmpDescription_achievement = _cursor.getString(_cursorIndexOfDescriptionAchievement);
        }
        final boolean _tmpUnlocked;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfUnlocked);
        _tmpUnlocked = _tmp != 0;
        final double _tmpProgress;
        _tmpProgress = _cursor.getDouble(_cursorIndexOfProgress);
        _item = new SingularAchievement(_tmpId_achievement,_tmpReceiver_id,_tmpName_achievement,_tmpDescription_achievement,_tmpUnlocked,_tmpProgress);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
