package com.skepticalone.armour.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.util.Pair;

import com.skepticalone.armour.data.db.Contract;
import com.skepticalone.armour.data.model.RosteredShiftEntity;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;

import java.util.List;
import java.util.Set;

@SuppressWarnings("NullableProblems")
@Dao
public abstract class RosteredShiftDao extends ItemDao<RosteredShiftEntity> {

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
    public abstract void setShiftTimesSync(long id, @NonNull Instant start, @NonNull Instant end, @Nullable Instant loggedStart, @Nullable Instant loggedEnd);

    @Override
    @Query("UPDATE " +
            Contract.RosteredShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    public abstract void setCommentSync(long id, @Nullable String comment);

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_START)
    public abstract LiveData<List<RosteredShiftEntity>> fetchItems();

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " IN(:ids)")
    abstract List<RosteredShiftEntity> fetchItemsInternalSync(@NonNull Set<Long> ids);

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

    @Nullable
    @Query("SELECT " +
            Contract.COLUMN_NAME_SHIFT_END +
            " FROM " +
            Contract.RosteredShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.COLUMN_NAME_SHIFT_END +
            " DESC LIMIT 1")
    @RestrictTo(RestrictTo.Scope.SUBCLASSES)
    abstract Instant getLastShiftEndInternalSync();

    @Transaction
    public long insertSync(@NonNull Pair<LocalTime, LocalTime> times, @NonNull ZoneId zoneId, boolean skipWeekends) {
        return insertSync(
                RosteredShiftEntity.from(getLastShiftEndInternalSync(), times, zoneId, skipWeekends)
        );
    }

}