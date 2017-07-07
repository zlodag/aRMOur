package com.skepticalone.mecachecker.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.model.Expense;

import org.joda.time.DateTime;

import java.math.BigDecimal;

@Entity(tableName = "expenses")
public class ExpenseEntity implements Expense {
//    @Ignore
//    public static final String
//            SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `expenses` (" +
//                    "`id` INTEGER PRIMARY KEY, " +
//                    "`title` TEXT NOT NULL, " +
//                    "`payment` INTEGER NOT NULL, " +
//                    "`comment` TEXT DEFAULT NULL, " +
//                    "`claimed` INTEGER DEFAULT NULL, " +
//                    "`paid` INTEGER DEFAULT NULL, " +
//                    "CHECK (length(`title`) > 0), " +
//                    "CHECK (`paid` IS NULL OR `claimed` IS NOT NULL), " +
//                    "CHECK (`claimed` <= `paid`), " +
//                    "CHECK (length(`comment`) > 0)" +
//                    ")",
//            SQL_DROP_TABLE = "DROP TABLE IF EXISTS `expenses`";

    @NonNull
    private final String title;
    @NonNull
    private final BigDecimal payment;
    @Nullable
    private final String comment;
    @PrimaryKey
    private long id = 0L;
    @Nullable
    private DateTime claimed = null;
    @Nullable
    private DateTime paid = null;

    public ExpenseEntity(
            @NonNull String title,
            @NonNull BigDecimal payment,
            @Nullable String comment) {
        this.title = title;
        this.payment = payment;
        this.comment = comment;
    }

    @Override
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return title;
    }

    @NonNull
    @Override
    public BigDecimal getPayment() {
        return payment;
    }

    @Nullable
    @Override
    public String getComment() {
        return comment;
    }

    @Nullable
    @Override
    public DateTime getClaimed() {
        return claimed;
    }

    public void setClaimed(@Nullable DateTime claimed) {
        this.claimed = claimed;
    }

    @Nullable
    @Override
    public DateTime getPaid() {
        return paid;
    }

    public void setPaid(@Nullable DateTime paid) {
        this.paid = paid;
    }
}