package com.skepticalone.mecachecker.components;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import static com.skepticalone.mecachecker.util.ShiftUtil.getClaimStatusIcon;

abstract class SinglePaymentItemListFragment extends ListFragment {

    private Callbacks mCallbacks;

    abstract int getColumnIndexClaimed();

    abstract int getColumnIndexPaid();

    abstract int getColumnIndexComment();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCallbacks.setFab(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    abstract void addItem();

    @NonNull
    abstract String getFirstLine(@NonNull Cursor cursor);

    @Override
    final void bindViewHolderToCursor(ListItemViewHolder holder, @NonNull Cursor cursor) {
        holder.bindPlain(
                0,
                getFirstLine(cursor),
                cursor.isNull(getColumnIndexComment()) ? null : cursor.getString(getColumnIndexComment()),
                null,
                getClaimStatusIcon(cursor, getColumnIndexClaimed(), getColumnIndexPaid())
        );
    }

    interface Callbacks {
        void setFab(@NonNull View.OnClickListener fabListener);
    }

}