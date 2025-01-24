package com.example.trailsnapv2.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.trailsnapv2.entities.Member;
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
public final class MemberDao_Impl implements MemberDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Member> __insertionAdapterOfMember;

  private final EntityDeletionOrUpdateAdapter<Member> __deletionAdapterOfMember;

  public MemberDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMember = new EntityInsertionAdapter<Member>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `members` (`id`,`user_id`,`party_id`,`join_date`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Member value) {
        stmt.bindLong(1, value.getMember_id());
        stmt.bindLong(2, value.getUser_id());
        stmt.bindLong(3, value.getParty_id());
        if (value.getJoin_date() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getJoin_date());
        }
      }
    };
    this.__deletionAdapterOfMember = new EntityDeletionOrUpdateAdapter<Member>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `members` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Member value) {
        stmt.bindLong(1, value.getMember_id());
      }
    };
  }

  @Override
  public long insert(final Member member) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfMember.insertAndReturnId(member);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Member member) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfMember.handle(member);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Member> getAll() {
    final String _sql = "SELECT * FROM members";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "user_id");
      final int _cursorIndexOfPartyId = CursorUtil.getColumnIndexOrThrow(_cursor, "party_id");
      final int _cursorIndexOfJoinDate = CursorUtil.getColumnIndexOrThrow(_cursor, "join_date");
      final List<Member> _result = new ArrayList<Member>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Member _item;
        final long _tmpId;
        _tmpId = _cursor.getLong(_cursorIndexOfId);
        final long _tmpUser_id;
        _tmpUser_id = _cursor.getLong(_cursorIndexOfUserId);
        final long _tmpParty_id;
        _tmpParty_id = _cursor.getLong(_cursorIndexOfPartyId);
        final String _tmpJoin_date;
        if (_cursor.isNull(_cursorIndexOfJoinDate)) {
          _tmpJoin_date = null;
        } else {
          _tmpJoin_date = _cursor.getString(_cursorIndexOfJoinDate);
        }
        _item = new Member(_tmpId,_tmpUser_id,_tmpParty_id,_tmpJoin_date);
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
