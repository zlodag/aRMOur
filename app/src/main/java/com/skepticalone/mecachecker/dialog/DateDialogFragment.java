package com.skepticalone.mecachecker.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;

import org.joda.time.LocalDate;

public abstract class DateDialogFragment<Entity, ViewModel extends ViewModelContract<Entity>> extends DialogFragment<Entity, ViewModel> implements DatePickerDialog.OnDateSetListener {

    private DatePickerDialog datePickerDialog;

    @Override
    @NonNull
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        LocalDate today = LocalDate.now();
        datePickerDialog = new DatePickerDialog(
                getActivity(), this,
                today.getYear(),
                today.getMonthOfYear() - 1,
                today.getDayOfMonth()
        );
        return datePickerDialog;
    }

    @NonNull
    abstract LocalDate getDateForDisplay(@NonNull Entity item);

    @Override
    final void onCurrentItemChanged(@NonNull Entity item) {
        LocalDate date = getDateForDisplay(item);
        datePickerDialog.updateDate(date.getYear(), date.getMonthOfYear() - 1, date.getDayOfMonth());
    }

    @Override
    public final void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        onDateSet(new LocalDate(year, month + 1, dayOfMonth));
    }

    abstract void onDateSet(@NonNull LocalDate date);

}
