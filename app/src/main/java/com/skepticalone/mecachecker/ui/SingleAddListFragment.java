package com.skepticalone.mecachecker.ui;

import android.support.annotation.Nullable;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.skepticalone.mecachecker.data.ItemCallbacks;
import com.skepticalone.mecachecker.model.PayableItem;

import java.util.List;

abstract class SingleAddListFragment<ItemType extends PayableItem, Entity extends ItemType, ViewModel extends ItemCallbacks<Entity>> extends ListFragment<ItemType, Entity, ViewModel> {

    private FloatingActionButton mFab;

    @Override
    final void setupFab(FabCallbacks callbacks) {
        callbacks.getFloatingActionMenu().hideMenu(true);
        mFab = callbacks.getFabAdd();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });
    }

    @Override
    public final void onDestroyView() {
        super.onDestroyView();
        mFab.hide(true);
    }

    @Override
    public final void onChanged(@Nullable List<Entity> entities) {
        super.onChanged(entities);
        if (entities == null) {
            mFab.hide(true);
        } else {
            mFab.show(true);
        }
    }

    abstract void addNewItem();

}

