package com.example.trailsnapv2.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.trailsnapv2.entities.Trail;
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
public final class TrailDao_Impl implements TrailDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Trail> __insertionAdapterOfTrail;

  private final EntityDeletionOrUpdateAdapter<Trail> __deletionAdapterOfTrail;

  public TrailDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrail = new EntityInsertionAdapter<Trail>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `trails` (`trail_id`,`user_id`,`trail_name`,`distance`,`start_time`,`end_time`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trail value) {
        stmt.bindLong(1, value.getTrail_id());
        stmt.bindLong(2, value.getUser_id());
        if (value.getTrail_name() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getTrail_name());
        }
        stmt.bindDouble(4, value.getDistance());
        if (value.getStart_time() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getStart_time());
        }
        if (value.getEnd_time() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getEnd_time());
        }
      }
    };
    this.__deletionAdapterOfTrail = new EntityDeletionOrUpdateAdapter<Trail>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `trails` WHERE `trail_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trail value) {
        stmt.bindLong(1, value.getTrail_id());
      }
    };
  }

  @Override
  public long insert(final Trail trail) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfTrail.insertAndReturnId(trail);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Trail trail) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfTrail.handle(trail);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Trail> getAll() {
    final String _sql = "SELECT * FROM trails";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfTrailId = CursorUtil.getColumnIndexOrThrow(_cursor, "trail_id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfTrailName = CursorUtil.getColumnIndexOrThrow(_cursor, "trail_name");
      final int _cursorIndexOfDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "distance");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
      final List<Trail> _result = new ArrayList<Trail>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trail _item;
        final long _tmpTrail_id;
        _tmpTrail_id = _cursor.getLong(_cursorIndexOfTrailId);
        final long _tmpUser_id;
        _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
        final String _tmpTrail_name;
        if (_cursor.isNull(_cursorIndexOfTrailName)) {
          _tmpTrail_name = null;
        } else {
          _tmpTrail_name = _cursor.getString(_cursorIndexOfTrailName);
        }
        final double _tmpDistance;
        _tmpDistance = _cursor.getDouble(_cursorIndexOfDistance);
        final String _tmpStart_time;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmpStart_time = null;
        } else {
          _tmpStart_time = _cursor.getString(_cursorIndexOfStartTime);
        }
        final String _tmpEnd_time;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmpEnd_time = null;
        } else {
          _tmpEnd_time = _cursor.getString(_cursorIndexOfEndTime);
        }
        _item = new Trail(_tmpTrail_id,_tmpUser_id,_tmpTrail_name,_tmpDistance,_tmpStart_time,_tmpEnd_time);
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
