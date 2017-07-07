package com.skepticalone.mecachecker.db.converter;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyConverter {

    private static final int MONEY_SCALE = 2;

    @TypeConverter
    public static int moneyToCents(@NonNull BigDecimal money) {
        return money.setScale(MONEY_SCALE, RoundingMode.HALF_UP).intValue();
    }

    @TypeConverter
    @NonNull
    public static BigDecimal centsToMoney(int cents) {
        return BigDecimal.valueOf(cents, MONEY_SCALE);
    }

}