package com.skepticalone.mecachecker.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteStatement;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.data.db.AppDatabase;
import com.skepticalone.mecachecker.data.db.Contract;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.data.util.DateTimeConverter;
import com.skepticalone.mecachecker.data.util.ShiftData;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.List;

@Dao
public abstract class RosteredShiftCustomDao extends CustomDao<RosteredShiftEntity> {

    @NonNull
    private final SupportSQLiteStatement insertStatement;
    @NonNull
    private static final String GET_LAST_SHIFT_END =
            "SELECT " + Contract.COLUMN_NAME_SHIFT_END + " FROM " +
                    Contract.RosteredShifts.TABLE_NAME +
                    " ORDER BY " +
                    Contract.COLUMN_NAME_SHIFT_END +
                    " DESC LIMIT 1";

    RosteredShiftCustomDao(@NonNull AppDatabase database) {
        super(database);
        insertStatement = getDatabase().compileStatement("INSERT INTO " +
                Contract.RosteredShifts.TABLE_NAME +
                " (" +
                Contract.COLUMN_NAME_SHIFT_START +
                ", " +
                Contract.COLUMN_NAME_SHIFT_END +
                ") VALUES (?,?)");
//        setTimesStatement = getDatabase().compileStatement("UPDATE " +
//                Contract.RosteredShifts.TABLE_NAME +
//                " SET " +
//                Contract.COLUMN_NAME_SHIFT_START +
//                " = ?, " +
//                Contract.COLUMN_NAME_SHIFT_START +
//                " = ?, " +
//                Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
//                " = ?, " +
//                Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
//                " = ? WHERE " +
//                BaseColumns._ID +
//                " = ?");
//        setLoggedStatement = getDatabase().compileStatement("UPDATE " +
//                Contract.RosteredShifts.TABLE_NAME +
//                " SET " +
//                Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
//                " = ?, " +
//                Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
//                " = ? WHERE " +
//                BaseColumns._ID +
//                " = ?");
    }

//    public final void setLogged(long id, boolean logged) {
//        if (logged) {
//            setLoggedStatement.bindString();
//        }
//    }

    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_SHIFT_START +
            " = :start, " +
            Contract.COLUMN_NAME_SHIFT_END +
            " = :end, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = :loggedStart, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = :loggedEnd WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setShiftTimesSync(long id, @NonNull DateTime start, @NonNull DateTime end, @Nullable DateTime loggedStart, @Nullable DateTime loggedEnd);

    synchronized public final long insertSync(@NonNull LocalTime startTime, @NonNull LocalTime endTime, boolean skipWeekends){
        Cursor cursor = getDatabase().query(GET_LAST_SHIFT_END, null);
        @Nullable final DateTime lastShiftEnd = cursor.moveToFirst() ? DateTimeConverter.millisToDateTime(cursor.getLong(0)) : null;
        cursor.close();
        ShiftData shiftData = ShiftData.withEarliestStartAfterMinimumDurationBetweenShifts(startTime, endTime, lastShiftEnd, skipWeekends);
        insertStatement.bindLong(1, shiftData.getStart().getMillis());
        insertStatement.bindLong(2, shiftData.getEnd().getMillis());
        getDatabase().beginTransaction();
        try {
            long id = insertStatement.executeInsert();
            getDatabase().setTransactionSuccessful();
            return id;
        } finally {
            getDatabase().endTransaction();
        }
    }


    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = " +
            Contract.COLUMN_NAME_SHIFT_START +
            ", " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = " +
            Contract.COLUMN_NAME_SHIFT_END +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void switchOnLogSync(long id);

    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_START +
            " = NULL, " +
            Contract.RosteredShifts.COLUMN_NAME_LOGGED_SHIFT_END +
            " = NULL WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void switchOffLogSync(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract LiveData<RosteredShiftEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    abstract RosteredShiftEntity getItemInternalSync(long id);

    @NonNull
    @Override
    final String getTableName() {
        return Contract.RosteredShifts.TABLE_NAME;
    }

    @Override
    @Query("SELECT * FROM " + Contract.RosteredShifts.TABLE_NAME + " ORDER BY " + Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<RosteredShiftEntity>> getItems();
}
