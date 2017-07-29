package com.skepticalone.mecachecker.ui;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.adapter.ItemDetailAdapter;
import com.skepticalone.mecachecker.data.model.Item;
import com.skepticalone.mecachecker.data.viewModel.ViewModelContract;
import com.skepticalone.mecachecker.dialog.ExpenseCommentDialogFragment;

abstract class DetailFragment<Entity extends Item, ViewModel extends ViewModelContract<Entity>> extends BaseFragment<ItemDetailAdapter<Entity>, ViewModel>
        implements ItemDetailAdapter.Callbacks {

    private static final String DIALOG_FRAGMENT = "DIALOG_FRAGMENT";

    static DetailFragment getNewDetailFragment(@IdRes int itemType) {
        if (itemType == R.id.additional) return new AdditionalShiftDetailFragment();
        if (itemType == R.id.cross_cover) return new CrossCoverDetailFragment();
        if (itemType == R.id.expenses) return new ExpenseDetailFragment();
        throw new IllegalStateException();
    }

    @Override
    final int getLayout() {
        return R.layout.detail_recycler;
    }

    @Override
    public final void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getViewModel().getCurrentItem().observe(this, new Observer<Entity>() {
                    @Override
                    public void onChanged(@Nullable Entity entity) {
                        getAdapter().setItem(entity);
                    }
                }
        );
    }

    final void showDialogFragment(DialogFragment dialogFragment) {
        dialogFragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public void changeComment() {
        showDialogFragment(new ExpenseCommentDialogFragment());
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getViewModel().errorMessage.addObserver(errorMessageObserver);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        getViewModel().errorMessage.deleteObserver(errorMessageObserver);
//    }
}
