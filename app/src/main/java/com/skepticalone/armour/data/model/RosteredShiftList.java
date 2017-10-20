package com.skepticalone.armour.data.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.ZoneId;

import java.util.ArrayList;
import java.util.List;

public final class RosteredShiftList extends ShiftList<RosteredShiftEntity, RosteredShift> {

    @NonNull
    private final LiveData<Compliance.Configuration> liveComplianceConfig;

    public RosteredShiftList(@NonNull Context context, @NonNull LiveData<List<RosteredShiftEntity>> liveRawRosteredShifts) {
        super(context, liveRawRosteredShifts);
        liveComplianceConfig = Compliance.LiveComplianceConfig.getInstance(context);
        addSource(liveComplianceConfig, new Observer<Compliance.Configuration>() {
            @Override
            public void onChanged(@Nullable Compliance.Configuration complianceConfig) {
                onUpdated(liveRawItems.getValue(), liveTimeZone.getValue(), liveShiftConfig.getValue(), complianceConfig);
            }
        });
    }

    @Override
    void onUpdated(@Nullable List<RosteredShiftEntity> rawRosteredShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig) {
        onUpdated(rawRosteredShifts, timeZone, shiftConfig, liveComplianceConfig.getValue());
    }

    private void onUpdated(@Nullable List<RosteredShiftEntity> rawRosteredShifts, @Nullable ZoneId timeZone, @Nullable Shift.ShiftType.Configuration shiftConfig, @Nullable Compliance.Configuration complianceConfig) {
        if (rawRosteredShifts != null && timeZone != null && shiftConfig != null && complianceConfig != null) {
            List<RosteredShift> rosteredShifts = new ArrayList<>(rawRosteredShifts.size());
            for (RosteredShiftEntity rawRosteredShift : rawRosteredShifts) {
                rosteredShifts.add(new RosteredShift(rawRosteredShift, timeZone, shiftConfig, rosteredShifts, complianceConfig));
            }
            setValue(rosteredShifts);
        }
    }

}
