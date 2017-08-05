package com.skepticalone.mecachecker.adapter;

import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.entity.RosteredShiftEntity;
import com.skepticalone.mecachecker.util.ShiftUtil;

import java.util.List;

public final class RosteredShiftTotalsAdapter extends ItemTotalsAdapter<RosteredShiftEntity> {

    private static final int
            ROW_NUMBER_ALL_SHIFT_TOTAL = 0,
            ROW_NUMBER_NORMAL_DAY = 1,
            ROW_NUMBER_LONG_DAY = 2,
            ROW_NUMBER_NIGHT_SHIFT = 3,
            ROW_NUMBER_CUSTOM_SHIFT = 4,
            ROW_COUNT = 5;

    @NonNull
    private final Callbacks callbacks;
    @NonNull
    private final ShiftUtil.Calculator calculator;

    public RosteredShiftTotalsAdapter(@NonNull Callbacks callbacks, @NonNull ShiftUtil.Calculator calculator) {
        this.callbacks = callbacks;
        this.calculator = calculator;
    }

    @Override
    int getTotalNumberTitle() {
        return R.string.all_shifts;
    }

    @Override
    int getRowNumberTotalNumber() {
        return ROW_NUMBER_ALL_SHIFT_TOTAL;
    }

    @Override
    int getRowCount() {
        return ROW_COUNT;
    }

    @Override
    boolean isFiltered() {
        return !callbacks.includeCompliant() || !callbacks.includeNonCompliant();
    }

    @Override
    boolean isIncluded(@NonNull RosteredShiftEntity shift) {
        return shift.isCompliant() ? callbacks.includeCompliant() : callbacks.includeNonCompliant();
    }

    @Override
    boolean bindViewHolder(@NonNull List<RosteredShiftEntity> allShifts, @NonNull ItemViewHolder holder, int position) {
        final ShiftUtil.ShiftType shiftType;
        switch (position) {
            case ROW_NUMBER_NORMAL_DAY:
                shiftType = ShiftUtil.ShiftType.NORMAL_DAY;
                break;
            case ROW_NUMBER_LONG_DAY:
                shiftType = ShiftUtil.ShiftType.LONG_DAY;
                break;
            case ROW_NUMBER_NIGHT_SHIFT:
                shiftType = ShiftUtil.ShiftType.NIGHT_SHIFT;
                break;
            case ROW_NUMBER_CUSTOM_SHIFT:
                shiftType = ShiftUtil.ShiftType.CUSTOM;
                break;
            default:
                return super.bindViewHolder(allShifts, holder, position);
        }
        List<RosteredShiftEntity> filteredShifts = calculator.getFilteredShifts(allShifts, shiftType);
        holder.setupTotals(
                ShiftUtil.getShiftIcon(shiftType),
                ShiftUtil.getShiftTitle(shiftType),
                getTotalNumber(filteredShifts, holder)
        );
        return true;
    }

    public interface Callbacks {
        boolean includeCompliant();
        boolean includeNonCompliant();
    }

}