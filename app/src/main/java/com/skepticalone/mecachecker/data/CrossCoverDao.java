package com.skepticalone.mecachecker.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.List;

@Dao
interface CrossCoverDao extends ItemDao<CrossCoverEntity>, PayableDao {

    @Override
    @Insert
    void insertItemSync(@NonNull CrossCoverEntity crossCover);

    @NonNull
    @Override
    @Query("SELECT * FROM " + Contract.CrossCoverShifts.TABLE_NAME + " ORDER BY " + Contract.CrossCoverShifts.COLUMN_NAME_DATE)
    LiveData<List<CrossCoverEntity>> getItems();

    @NonNull
    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    LiveData<CrossCoverEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    CrossCoverEntity getItemSync(long id);

    @Override
    @Query("DELETE FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    int deleteItemSync(long id);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setCommentSync(long id, @Nullable String comment);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaymentSync(long id, @NonNull BigDecimal payment);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimedSync(long id, @Nullable DateTime claimed);

    @Override
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaidSync(long id, @Nullable DateTime paid);

    @Nullable
    @WorkerThread
    @Query("SELECT " + Contract.CrossCoverShifts.COLUMN_NAME_DATE + " FROM " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " ORDER BY " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " DESC LIMIT 1")
    LocalDate getLastCrossCoverDateSync();

    @WorkerThread
    @Query("UPDATE " +
            Contract.CrossCoverShifts.TABLE_NAME +
            " SET " +
            Contract.CrossCoverShifts.COLUMN_NAME_DATE +
            " = :date WHERE " +
            BaseColumns._ID +
            " = :id")
    void setDateSync(long id, @NonNull LocalDate date);

}
