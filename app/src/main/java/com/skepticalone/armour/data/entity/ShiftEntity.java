package com.skepticalone.armour.data.entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.armour.data.model.Shift;
import com.skepticalone.armour.util.ShiftType;


abstract class ShiftEntity extends ItemEntity implements Shift {

    @NonNull
    @Embedded
    private final ShiftData shiftData;
    @Ignore
    private ShiftType shiftType;

    ShiftEntity(@NonNull ShiftData shiftData, @Nullable String comment) {
        super(comment);
        this.shiftData = shiftData;
    }

    @NonNull
    @Override
    public final ShiftData getShiftData() {
        return shiftData;
    }

    @NonNull
    @Override
    public ShiftType getShiftType() {
        return shiftType;
    }

    static final class ShiftTypeConfiguration implements ShiftTypeCalculator {

        private final int
                normalDayStart,
                normalDayEnd,
                longDayStart,
                longDayEnd,
                nightShiftStart,
                nightShiftEnd;

        ShiftTypeConfiguration(
                int normalDayStart,
                int normalDayEnd,
                int longDayStart,
                int longDayEnd,
                int nightShiftStart,
                int nightShiftEnd
        ) {
            this.normalDayStart = normalDayStart;
            this.normalDayEnd = normalDayEnd;
            this.longDayStart = longDayStart;
            this.longDayEnd = longDayEnd;
            this.nightShiftStart = nightShiftStart;
            this.nightShiftEnd = nightShiftEnd;
        }

        @Override
        public void process(@NonNull ShiftEntity shift) {
            int start = shift.shiftData.getStart().getMinuteOfDay(), end = shift.shiftData.getEnd().getMinuteOfDay();
            if (start == normalDayStart && end == normalDayEnd) {
                shift.shiftType = ShiftType.NORMAL_DAY;
            } else if (start == longDayStart && end == longDayEnd) {
                shift.shiftType = ShiftType.LONG_DAY;
            } else if (start == nightShiftStart && end == nightShiftEnd) {
                shift.shiftType = ShiftType.NIGHT_SHIFT;
            } else {
                shift.shiftType = ShiftType.CUSTOM;
            }
        }
    }
}
