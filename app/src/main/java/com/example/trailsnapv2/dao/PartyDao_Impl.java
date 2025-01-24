package com.example.trailsnapv2.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.trailsnapv2.entities.Party;
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
public final class PartyDao_Impl implements PartyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Party> __insertionAdapterOfParty;

  private final EntityDeletionOrUpdateAdapter<Party> __deletionAdapterOfParty;

  public PartyDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfParty = new EntityInsertionAdapter<Party>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `party` (`party_id`,`party_name`,`party_description`,`creation_date`,`creator_id`,`total_distance`,`time_used`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Party value) {
        stmt.bindLong(1, value.getParty_id());
        if (value.getParty_name() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getParty_name());
        }
        if (value.getParty_description() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getParty_description());
        }
        if (value.getCreation_date() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getCreation_date());
        }
        stmt.bindLong(5, value.getCreator_id());
        stmt.bindDouble(6, value.getTotal_distance());
        stmt.bindLong(7, value.getTime_used());
      }
    };
    this.__deletionAdapterOfParty = new EntityDeletionOrUpdateAdapter<Party>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `party` WHERE `party_id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Party value) {
        stmt.bindLong(1, value.getParty_id());
      }
    };
  }

  @Override
  public long insert(final Party party) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfParty.insertAndReturnId(party);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Party party) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfParty.handle(party);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Party> getAll() {
    final String _sql = "SELECT * FROM party";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfPartyId = CursorUtil.getColumnIndexOrThrow(_cursor, "party_id");
      final int _cursorIndexOfPartyName = CursorUtil.getColumnIndexOrThrow(_cursor, "party_name");
      final int _cursorIndexOfPartyDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "party_description");
      final int _cursorIndexOfCreationDate = CursorUtil.getColumnIndexOrThrow(_cursor, "creation_date");
      final int _cursorIndexOfCreatorId = CursorUtil.getColumnIndexOrThrow(_cursor, "creator_id");
      final int _cursorIndexOfTotalDistance = CursorUtil.getColumnIndexOrThrow(_cursor, "total_distance");
      final int _cursorIndexOfTimeUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "time_used");
      final List<Party> _result = new ArrayList<Party>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Party _item;
        final long _tmpParty_id;
        _tmpParty_id = _cursor.getLong(_cursorIndexOfPartyId);
        final String _tmpParty_name;
        if (_cursor.isNull(_cursorIndexOfPartyName)) {
          _tmpParty_name = null;
        } else {
          _tmpParty_name = _cursor.getString(_cursorIndexOfPartyName);
        }
        final String _tmpParty_description;
        if (_cursor.isNull(_cursorIndexOfPartyDescription)) {
          _tmpParty_description = null;
        } else {
          _tmpParty_description = _cursor.getString(_cursorIndexOfPartyDescription);
        }
        final String _tmpCreation_date;
        if (_cursor.isNull(_cursorIndexOfCreationDate)) {
          _tmpCreation_date = null;
        } else {
          _tmpCreation_date = _cursor.getString(_cursorIndexOfCreationDate);
        }
        final long _tmpCreator_id;
        _tmpCreator_id = _cursor.getLong(_cursorIndexOfCreatorId);
        final double _tmpTotal_distance;
        _tmpTotal_distance = _cursor.getDouble(_cursorIndexOfTotalDistance);
        final long _tmpTime_used;
        _tmpTime_used = _cursor.getLong(_cursorIndexOfTimeUsed);
        _item = new Party(_tmpParty_id,_tmpParty_name,_tmpParty_description,_tmpCreation_date,_tmpCreator_id,_tmpTotal_distance,_tmpTime_used);
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
