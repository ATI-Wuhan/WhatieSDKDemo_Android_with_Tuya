package com.whatie.ati.androiddemo.database.greenDao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.SqlUtils;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import com.whatie.ati.androiddemo.database.entity.HomeDB;

import com.whatie.ati.androiddemo.database.entity.RoomDB;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "ROOM_DB".
*/
public class RoomDBDao extends AbstractDao<RoomDB, Long> {

    public static final String TABLENAME = "ROOM_DB";

    /**
     * Properties of entity RoomDB.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property RoomId = new Property(0, Long.class, "roomId", true, "_id");
        public final static Property HomeId = new Property(1, Long.class, "homeId", false, "HOME_ID");
        public final static Property RoomJson = new Property(2, String.class, "roomJson", false, "ROOM_JSON");
    }

    private DaoSession daoSession;

    private Query<RoomDB> homeDB_RoomDBsQuery;

    public RoomDBDao(DaoConfig config) {
        super(config);
    }
    
    public RoomDBDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"ROOM_DB\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: roomId
                "\"HOME_ID\" INTEGER," + // 1: homeId
                "\"ROOM_JSON\" TEXT);"); // 2: roomJson
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"ROOM_DB\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, RoomDB entity) {
        stmt.clearBindings();
 
        Long roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindLong(1, roomId);
        }
 
        Long homeId = entity.getHomeId();
        if (homeId != null) {
            stmt.bindLong(2, homeId);
        }
 
        String roomJson = entity.getRoomJson();
        if (roomJson != null) {
            stmt.bindString(3, roomJson);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, RoomDB entity) {
        stmt.clearBindings();
 
        Long roomId = entity.getRoomId();
        if (roomId != null) {
            stmt.bindLong(1, roomId);
        }
 
        Long homeId = entity.getHomeId();
        if (homeId != null) {
            stmt.bindLong(2, homeId);
        }
 
        String roomJson = entity.getRoomJson();
        if (roomJson != null) {
            stmt.bindString(3, roomJson);
        }
    }

    @Override
    protected final void attachEntity(RoomDB entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public RoomDB readEntity(Cursor cursor, int offset) {
        RoomDB entity = new RoomDB( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // roomId
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // homeId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // roomJson
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, RoomDB entity, int offset) {
        entity.setRoomId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHomeId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setRoomJson(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(RoomDB entity, long rowId) {
        entity.setRoomId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(RoomDB entity) {
        if(entity != null) {
            return entity.getRoomId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(RoomDB entity) {
        return entity.getRoomId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "roomDBs" to-many relationship of HomeDB. */
    public List<RoomDB> _queryHomeDB_RoomDBs(Long homeId) {
        synchronized (this) {
            if (homeDB_RoomDBsQuery == null) {
                QueryBuilder<RoomDB> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.HomeId.eq(null));
                homeDB_RoomDBsQuery = queryBuilder.build();
            }
        }
        Query<RoomDB> query = homeDB_RoomDBsQuery.forCurrentThread();
        query.setParameter(0, homeId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getHomeDBDao().getAllColumns());
            builder.append(" FROM ROOM_DB T");
            builder.append(" LEFT JOIN HOME_DB T0 ON T.\"HOME_ID\"=T0.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected RoomDB loadCurrentDeep(Cursor cursor, boolean lock) {
        RoomDB entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        HomeDB homeDB = loadCurrentOther(daoSession.getHomeDBDao(), cursor, offset);
        entity.setHomeDB(homeDB);

        return entity;    
    }

    public RoomDB loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<RoomDB> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<RoomDB> list = new ArrayList<RoomDB>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<RoomDB> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<RoomDB> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
