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

import java.math.BigDecimal;
import java.util.List;

@Dao
interface ExpenseDao extends ItemDao<ExpenseEntity>, PayableDao {

    @Override
    @Insert
    void insertItemSync(@NonNull ExpenseEntity expense);

    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME + " " +
            "ORDER BY " +
            Contract.COLUMN_NAME_PAID +
            " IS NULL, " +
            Contract.COLUMN_NAME_CLAIMED +
            " IS NULL, ifnull(" +
            Contract.COLUMN_NAME_PAID +
            ", " +
            Contract.COLUMN_NAME_CLAIMED +
            ")")
    @NonNull
    LiveData<List<ExpenseEntity>> getItems();

    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    @NonNull
    LiveData<ExpenseEntity> getItem(long id);

    @Override
    @Query("SELECT * FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    ExpenseEntity getItemSync(long id);

    @Override
    @Query("DELETE FROM " +
            Contract.Expenses.TABLE_NAME +
            " WHERE " +
            BaseColumns._ID +
            " = :id")
    int deleteItemSync(long id);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_COMMENT +
            " = :comment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setCommentSync(long id, @Nullable String comment);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAYMENT +
            " = :payment WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaymentSync(long id, @NonNull BigDecimal payment);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_CLAIMED +
            " = :claimed WHERE " +
            BaseColumns._ID +
            " = :id")
    void setClaimedSync(long id, @Nullable DateTime claimed);

    @Override
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.COLUMN_NAME_PAID +
            " = :paid WHERE " +
            BaseColumns._ID +
            " = :id")
    void setPaidSync(long id, @Nullable DateTime paid);

    @WorkerThread
    @Query("UPDATE " +
            Contract.Expenses.TABLE_NAME +
            " SET " +
            Contract.Expenses.COLUMN_NAME_TITLE +
            " = :title WHERE " +
            BaseColumns._ID +
            " = :id")
    void setTitleSync(long id, @NonNull String title);

}