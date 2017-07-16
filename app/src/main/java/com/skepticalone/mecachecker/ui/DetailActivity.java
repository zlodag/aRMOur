package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.AdditionalShiftViewModel;
import com.skepticalone.mecachecker.data.CrossCoverViewModel;
import com.skepticalone.mecachecker.data.ExpenseViewModel;

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
        int title;
        long itemId = getIntent().getLongExtra(ITEM_ID, NO_ID);
        switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
            case Constants.ITEM_TYPE_ADDITIONAL_SHIFT:
                title = R.string.additional_shift;
                ViewModelProviders.of(this).get(AdditionalShiftViewModel.class).selectItem(itemId);
                break;
            case Constants.ITEM_TYPE_CROSS_COVER:
                title = R.string.cross_cover;
                ViewModelProviders.of(this).get(CrossCoverViewModel.class).selectItem(itemId);
                break;
            case Constants.ITEM_TYPE_EXPENSE:
                title = R.string.expense;
                ViewModelProviders.of(this).get(ExpenseViewModel.class).selectItem(itemId);
                break;
            default:
                throw new IllegalStateException();
        }
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(title);
        if (savedInstanceState == null) {
            DetailFragment detailFragment;
            switch (getIntent().getIntExtra(ITEM_TYPE, NO_ITEM_TYPE)) {
                case Constants.ITEM_TYPE_ADDITIONAL_SHIFT:
                    detailFragment = new AdditionalShiftDetailFragment();
                    break;
                case Constants.ITEM_TYPE_CROSS_COVER:
                    detailFragment = new CrossCoverDetailFragment();
                    break;
                case Constants.ITEM_TYPE_EXPENSE:
                    detailFragment = new ExpenseDetailFragment();
                    break;
                default:
                    throw new IllegalStateException();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.coordinator, detailFragment, DETAIL_FRAGMENT)
                    .commit();
        }
    }

}
