package com.example.trailsnapv2.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.trailsnapv2.entities.PartyAchievement;
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
public final class PartyAchievementDao_Impl implements PartyAchievementDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PartyAchievement> __insertionAdapterOfPartyAchievement;

  private final EntityDeletionOrUpdateAdapter<PartyAchievement> __deletionAdapterOfPartyAchievement;

  public PartyAchievementDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPartyAchievement = new EntityInsertionAdapter<PartyAchievement>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `party_achievements` (`id_achievement`,`party_id`,`name_achievement`,`description_achievement`,`unlocked`,`progress`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PartyAchievement value) {
        stmt.bindLong(1, value.getId_achievement());
        stmt.bindLong(2, value.getParty_id());
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
        stmt.bindLong(5, value.isUnlocked() ? 1 : 0);
        stmt.bindDouble(6, value.getProgress());
      }
    };
    this.__deletionAdapterOfPartyAchievement = new EntityDeletionOrUpdateAdapter<PartyAchievement>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `party_achievements` WHERE `id_achievement` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PartyAchievement value) {
        stmt.bindLong(1, value.getId_achievement());
      }
    };
  }

  @Override
  public long insert(final PartyAchievement partyAchievement) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfPartyAchievement.insertAndReturnId(partyAchievement);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final PartyAchievement partyAchievement) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfPartyAchievement.handle(partyAchievement);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<PartyAchievement> getAll() {
    final String _sql = "SELECT * FROM party_achievements";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfIdAchievement = CursorUtil.getColumnIndexOrThrow(_cursor, "id_achievement");
      final int _cursorIndexOfPartyId = CursorUtil.getColumnIndexOrThrow(_cursor, "party_id");
      final int _cursorIndexOfNameAchievement = CursorUtil.getColumnIndexOrThrow(_cursor, "name_achievement");
      final int _cursorIndexOfDescriptionAchievement = CursorUtil.getColumnIndexOrThrow(_cursor, "description_achievement");
      final int _cursorIndexOfUnlocked = CursorUtil.getColumnIndexOrThrow(_cursor, "unlocked");
      final int _cursorIndexOfProgress = CursorUtil.getColumnIndexOrThrow(_cursor, "progress");
      final List<PartyAchievement> _result = new ArrayList<PartyAchievement>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final PartyAchievement _item;
        final long _tmpId_achievement;
        _tmpId_achievement = _cursor.getLong(_cursorIndexOfIdAchievement);
        final long _tmpParty_id;
        _tmpParty_id = _cursor.getLong(_cursorIndexOfPartyId);
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
        _item = new PartyAchievement(_tmpId_achievement, _tmpParty_id, _tmpName_achievement, _tmpDescription_achievement, _tmpUnlocked, _tmpProgress);
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