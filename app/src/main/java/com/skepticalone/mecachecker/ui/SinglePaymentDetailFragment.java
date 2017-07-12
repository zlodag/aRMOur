package com.skepticalone.mecachecker.ui;

import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.skepticalone.mecachecker.R;
import com.skepticalone.mecachecker.data.PayableViewModel;
import com.skepticalone.mecachecker.model.PayableItem;
import com.skepticalone.mecachecker.ui.adapter.PayableCallbacks;
import com.skepticalone.mecachecker.ui.components.PlainTextDialogFragment;

abstract class SinglePaymentDetailFragment<ItemType extends PayableItem, Entity extends ItemType, ViewModel extends PayableViewModel<Entity>> extends DetailFragment<ItemType, Entity, ViewModel> implements PayableCallbacks, PlainTextDialogFragment.Callbacks {

    final void changeString(long itemId, @Nullable String currentString, @StringRes int title, boolean isTitle) {
        PlainTextDialogFragment fragment = PlainTextDialogFragment.newInstance(itemId, currentString, title, isTitle);
        fragment.setTargetFragment(this, 0);
        fragment.show(getFragmentManager(), DIALOG_FRAGMENT);
    }

    @Override
    public final void changeComment(long itemId, @Nullable String currentComment) {
        changeString(itemId, currentComment, R.string.comment, false);
    }

    @Override
    @CallSuper
    public void savePlainText(long itemId, @Nullable String trimmedTextWithLength, boolean isTitle) {
        if (!isTitle) {
            getViewModel().setComment(itemId, trimmedTextWithLength);
        }
    }

    @Override
    public final void setClaimed(long itemId, boolean claimed) {
        getViewModel().setClaimed(itemId, claimed);
    }

    @Override
    public final void setPaid(long itemId, boolean paid) {
        getViewModel().setPaid(itemId, paid);
    }
}