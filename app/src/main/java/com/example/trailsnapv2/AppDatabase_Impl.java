package com.example.trailsnapv2;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.trailsnapv2.dao.MemberDao;
import com.example.trailsnapv2.dao.MemberDao_Impl;
import com.example.trailsnapv2.dao.PartyAchievementDao;
import com.example.trailsnapv2.dao.PartyAchievementDao_Impl;
import com.example.trailsnapv2.dao.PartyDao;
import com.example.trailsnapv2.dao.PartyDao_Impl;
import com.example.trailsnapv2.dao.SingularAchievementDao;
import com.example.trailsnapv2.dao.SingularAchievementDao_Impl;
import com.example.trailsnapv2.dao.TrailDao;
import com.example.trailsnapv2.dao.TrailDao_Impl;
import com.example.trailsnapv2.dao.UserDao;
import com.example.trailsnapv2.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile TrailDao _trailDao;

  private volatile MemberDao _memberDao;

  private volatile PartyDao _partyDao;

  private volatile SingularAchievementDao _singularAchievementDao;

  private volatile PartyAchievementDao _partyAchievementDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `users` (`user_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `password` TEXT NOT NULL, `username` TEXT NOT NULL, `user_description` TEXT NOT NULL, `birthday` TEXT NOT NULL, `total_distance` REAL NOT NULL, `time_used` INTEGER NOT NULL, `creation_date` TEXT NOT NULL, `profile_picture` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `trails` (`trail_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `trail_name` TEXT NOT NULL, `distance` REAL NOT NULL, `start_time` TEXT NOT NULL, `end_time` TEXT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `members` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `user_id` INTEGER NOT NULL, `party_id` INTEGER NOT NULL, `join_date` TEXT NOT NULL, FOREIGN KEY(`user_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE , FOREIGN KEY(`party_id`) REFERENCES `party`(`party_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `party` (`party_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `party_name` TEXT NOT NULL, `party_description` TEXT NOT NULL, `creation_date` TEXT NOT NULL, `creator_id` INTEGER NOT NULL, `total_distance` REAL NOT NULL, `time_used` INTEGER NOT NULL, FOREIGN KEY(`creator_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `singular_achievements` (`id_achievement` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `achievement_type` TEXT NOT NULL, `receiver_id` INTEGER NOT NULL, `name_achievement` TEXT NOT NULL, `description_achievement` TEXT NOT NULL, `unlocked` INTEGER NOT NULL, `progress` REAL NOT NULL, FOREIGN KEY(`receiver_id`) REFERENCES `users`(`user_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `party_achievements` (`id_achievement` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `achievement_type` TEXT NOT NULL, `party_id` INTEGER NOT NULL, `name_achievement` TEXT NOT NULL, `description_achievement` TEXT NOT NULL, `unlocked` INTEGER NOT NULL, `progress` REAL NOT NULL, FOREIGN KEY(`party_id`) REFERENCES `party`(`party_id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '6acfd698bf8f28fd29171901a9893370')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `users`");
        _db.execSQL("DROP TABLE IF EXISTS `trails`");
        _db.execSQL("DROP TABLE IF EXISTS `members`");
        _db.execSQL("DROP TABLE IF EXISTS `party`");
        _db.execSQL("DROP TABLE IF EXISTS `singular_achievements`");
        _db.execSQL("DROP TABLE IF EXISTS `party_achievements`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        _db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(9);
        _columnsUsers.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("password", new TableInfo.Column("password", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("username", new TableInfo.Column("username", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("user_description", new TableInfo.Column("user_description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("birthday", new TableInfo.Column("birthday", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("total_distance", new TableInfo.Column("total_distance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("time_used", new TableInfo.Column("time_used", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("creation_date", new TableInfo.Column("creation_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("profile_picture", new TableInfo.Column("profile_picture", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(_db, "users");
        if (! _infoUsers.equals(_existingUsers)) {
          return new RoomOpenHelper.ValidationResult(false, "users(com.example.trailsnapv2.entities.User).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final HashMap<String, TableInfo.Column> _columnsTrails = new HashMap<String, TableInfo.Column>(6);
        _columnsTrails.put("trail_id", new TableInfo.Column("trail_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrails.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrails.put("trail_name", new TableInfo.Column("trail_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrails.put("distance", new TableInfo.Column("distance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrails.put("start_time", new TableInfo.Column("start_time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrails.put("end_time", new TableInfo.Column("end_time", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrails = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysTrails.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesTrails = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrails = new TableInfo("trails", _columnsTrails, _foreignKeysTrails, _indicesTrails);
        final TableInfo _existingTrails = TableInfo.read(_db, "trails");
        if (! _infoTrails.equals(_existingTrails)) {
          return new RoomOpenHelper.ValidationResult(false, "trails(com.example.trailsnapv2.entities.Trail).\n"
                  + " Expected:\n" + _infoTrails + "\n"
                  + " Found:\n" + _existingTrails);
        }
        final HashMap<String, TableInfo.Column> _columnsMembers = new HashMap<String, TableInfo.Column>(4);
        _columnsMembers.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("user_id", new TableInfo.Column("user_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("party_id", new TableInfo.Column("party_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMembers.put("join_date", new TableInfo.Column("join_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMembers = new HashSet<TableInfo.ForeignKey>(2);
        _foreignKeysMembers.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("user_id"), Arrays.asList("user_id")));
        _foreignKeysMembers.add(new TableInfo.ForeignKey("party", "CASCADE", "NO ACTION", Arrays.asList("party_id"), Arrays.asList("party_id")));
        final HashSet<TableInfo.Index> _indicesMembers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMembers = new TableInfo("members", _columnsMembers, _foreignKeysMembers, _indicesMembers);
        final TableInfo _existingMembers = TableInfo.read(_db, "members");
        if (! _infoMembers.equals(_existingMembers)) {
          return new RoomOpenHelper.ValidationResult(false, "members(com.example.trailsnapv2.entities.Member).\n"
                  + " Expected:\n" + _infoMembers + "\n"
                  + " Found:\n" + _existingMembers);
        }
        final HashMap<String, TableInfo.Column> _columnsParty = new HashMap<String, TableInfo.Column>(7);
        _columnsParty.put("party_id", new TableInfo.Column("party_id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("party_name", new TableInfo.Column("party_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("party_description", new TableInfo.Column("party_description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("creation_date", new TableInfo.Column("creation_date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("creator_id", new TableInfo.Column("creator_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("total_distance", new TableInfo.Column("total_distance", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsParty.put("time_used", new TableInfo.Column("time_used", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysParty = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysParty.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("creator_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesParty = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoParty = new TableInfo("party", _columnsParty, _foreignKeysParty, _indicesParty);
        final TableInfo _existingParty = TableInfo.read(_db, "party");
        if (! _infoParty.equals(_existingParty)) {
          return new RoomOpenHelper.ValidationResult(false, "party(com.example.trailsnapv2.entities.Party).\n"
                  + " Expected:\n" + _infoParty + "\n"
                  + " Found:\n" + _existingParty);
        }
        final HashMap<String, TableInfo.Column> _columnsSingularAchievements = new HashMap<String, TableInfo.Column>(7);
        _columnsSingularAchievements.put("id_achievement", new TableInfo.Column("id_achievement", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSingularAchievements.put("achievement_type", new TableInfo.Column("achievement_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSingularAchievements.put("receiver_id", new TableInfo.Column("receiver_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSingularAchievements.put("name_achievement", new TableInfo.Column("name_achievement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSingularAchievements.put("description_achievement", new TableInfo.Column("description_achievement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSingularAchievements.put("unlocked", new TableInfo.Column("unlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSingularAchievements.put("progress", new TableInfo.Column("progress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSingularAchievements = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSingularAchievements.add(new TableInfo.ForeignKey("users", "CASCADE", "NO ACTION", Arrays.asList("receiver_id"), Arrays.asList("user_id")));
        final HashSet<TableInfo.Index> _indicesSingularAchievements = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSingularAchievements = new TableInfo("singular_achievements", _columnsSingularAchievements, _foreignKeysSingularAchievements, _indicesSingularAchievements);
        final TableInfo _existingSingularAchievements = TableInfo.read(_db, "singular_achievements");
        if (! _infoSingularAchievements.equals(_existingSingularAchievements)) {
          return new RoomOpenHelper.ValidationResult(false, "singular_achievements(com.example.trailsnapv2.entities.SingularAchievement).\n"
                  + " Expected:\n" + _infoSingularAchievements + "\n"
                  + " Found:\n" + _existingSingularAchievements);
        }
        final HashMap<String, TableInfo.Column> _columnsPartyAchievements = new HashMap<String, TableInfo.Column>(7);
        _columnsPartyAchievements.put("id_achievement", new TableInfo.Column("id_achievement", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPartyAchievements.put("achievement_type", new TableInfo.Column("achievement_type", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPartyAchievements.put("party_id", new TableInfo.Column("party_id", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPartyAchievements.put("name_achievement", new TableInfo.Column("name_achievement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPartyAchievements.put("description_achievement", new TableInfo.Column("description_achievement", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPartyAchievements.put("unlocked", new TableInfo.Column("unlocked", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPartyAchievements.put("progress", new TableInfo.Column("progress", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPartyAchievements = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPartyAchievements.add(new TableInfo.ForeignKey("party", "CASCADE", "NO ACTION", Arrays.asList("party_id"), Arrays.asList("party_id")));
        final TableInfo _infoPartyAchievements = new TableInfo("party_achievements", _columnsPartyAchievements, _foreignKeysPartyAchievements);
        final TableInfo _existingPartyAchievements = TableInfo.read(_db, "party_achievements");
        if (! _infoPartyAchievements.equals(_existingPartyAchievements)) {
          return new RoomOpenHelper.ValidationResult(false, "party_achievements(com.example.trailsnapv2.entities.PartyAchievement).\n"
                  + " Expected:\n" + _infoPartyAchievements + "\n"
                  + " Found:\n" + _existingPartyAchievements);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "6acfd698bf8f28fd29171901a9893370", "cd7faf64430e92c7d870f44b80c33a49");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
            .name(configuration.name)
            .callback(_openCallback)
            .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "users", "trails", "members", "party", "singular_achievements", "party_achievements");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `users`");
      _db.execSQL("DELETE FROM `trails`");
      _db.execSQL("DELETE FROM `members`");
      _db.execSQL("DELETE FROM `party`");
      _db.execSQL("DELETE FROM `singular_achievements`");
      _db.execSQL("DELETE FROM `party_achievements`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TrailDao.class, TrailDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(MemberDao.class, MemberDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PartyDao.class, PartyDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SingularAchievementDao.class, SingularAchievementDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PartyAchievementDao.class, PartyAchievementDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(@NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public TrailDao trailDao() {
    if (_trailDao != null) {
      return _trailDao;
    } else {
      synchronized(this) {
        if(_trailDao == null) {
          _trailDao = new TrailDao_Impl(this);
        }
        return _trailDao;
      }
    }
  }

  @Override
  public MemberDao memberDao() {
    if (_memberDao != null) {
      return _memberDao;
    } else {
      synchronized(this) {
        if(_memberDao == null) {
          _memberDao = new MemberDao_Impl(this);
        }
        return _memberDao;
      }
    }
  }

  @Override
  public PartyDao partyDao() {
    if (_partyDao != null) {
      return _partyDao;
    } else {
      synchronized(this) {
        if(_partyDao == null) {
          _partyDao = new PartyDao_Impl(this);
        }
        return _partyDao;
      }
    }
  }

  @Override
  public SingularAchievementDao singularAchievementDao() {
    if (_singularAchievementDao != null) {
      return _singularAchievementDao;
    } else {
      synchronized(this) {
        if(_singularAchievementDao == null) {
          _singularAchievementDao = new SingularAchievementDao_Impl(this);
        }
        return _singularAchievementDao;
      }
    }
  }

  @Override
  public PartyAchievementDao partyAchievementDao() {
    if (_partyAchievementDao != null) {
      return _partyAchievementDao;
    } else {
      synchronized(this) {
        if(_partyAchievementDao == null) {
          _partyAchievementDao = new PartyAchievementDao_Impl(this);
        }
        return _partyAchievementDao;
      }
    }
  }
}