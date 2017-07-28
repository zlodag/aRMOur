package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.viewModel.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.viewModel.ExpenseViewModel;
import com.skepticalone.mecachecker.data.viewModel.ItemViewModel;

public final class DetailActivity extends CoordinatorActivity {

    static final String ITEM_TYPE = "ITEM_TYPE";
    static final String ITEM_ID = "ITEM_ID";
    private static final int NO_ITEM_TYPE = 0;
    private static final String DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private static final long NO_ID = -1L;

    @Override
    int getContentView() {
        return R.layout.detail_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int itemType = getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE), title;
        final Class<? extends ItemViewModel> c;
        switch (itemType) {
//            case Constants.ITEM_TYPE_ROSTERED_SHIFT:
//                title = R.string.rostered_shift;
//                c = RosteredShiftViewModel.class;
//                break;
//            case Constants.ITEM_TYPE_ADDITIONAL_SHIFT:
//                title = R.string.additional_shift;
//                c = AdditionalShiftViewModel.class;
//                break;
            case R.id.cross_cover:
                title = R.string.cross_cover;
                c = CrossCoverViewModel.class;
                break;
            case R.id.expenses:
                title = R.string.expense;
                c = ExpenseViewModel.class;
                break;
            default:
                throw new IllegalStateException();
        }
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(title);
        ViewModelProviders.of(this).get(c).selectItem(getIntent().getLongExtra(ITEM_ID, NO_ID));
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, DetailFragment.getNewDetailFragment(itemType), DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
