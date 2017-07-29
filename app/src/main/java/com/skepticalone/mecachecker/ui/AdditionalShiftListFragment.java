package com.skepticalone.mecachecker.ui;


import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.AdditionalShiftListAdapter;
import com.skepticalone.mecachecker.adapter.ItemListAdapter;
import com.skepticalone.mecachecker.data.entity.AdditionalShiftEntity;
import com.skepticalone.mecachecker.data.viewModel.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.util.ShiftUtil;

public final class AdditionalShiftListFragment extends ShiftAddListFragment<AdditionalShiftEntity, AdditionalShiftViewModel> {

    @Override
    int getItemType() {
        return R.id.additional;
    }

    @NonNull
    @Override
    ItemListAdapter<AdditionalShiftEntity> createAdapter(Context context) {
        return new AdditionalShiftListAdapter(this, new ShiftUtil.Calculator(context));
    }

    @NonNull
    @Override
    AdditionalShiftViewModel createViewModel(ViewModelProvider provider) {
        return provider.get(AdditionalShiftViewModel.class);
    }

}
