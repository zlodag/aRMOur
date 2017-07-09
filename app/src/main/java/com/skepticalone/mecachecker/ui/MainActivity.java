package com.skepticalone.mecachecker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.skepticalone.mecachecker.R;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener, ListFragment.Callbacks {

    private static final String LIST_FRAGMENT = "LIST_FRAGMENT", DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    //    private static final String MASTER_TO_DETAIL = "MASTER_TO_DETAIL";
//    private static final String TAG = "MainActivity";
    private FloatingActionMenu mFabMenu;
    private FloatingActionButton mFabNormalDay, mFabLongDay, mFabNightShift;
    private boolean mTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);
        navigation.setOnNavigationItemReselectedListener(this);
        mTwoPane = findViewById(R.id.detail_fragment_container) != null;
//        Log.i(TAG, "onCreate: mTwoPane = " + mTwoPane);
        mFabMenu = findViewById(R.id.fab_menu);
        mFabNormalDay = mFabMenu.findViewById(R.id.fab_normal_day);
        mFabLongDay = mFabMenu.findViewById(R.id.fab_long_day);
        mFabNightShift = mFabMenu.findViewById(R.id.fab_night_shift);
        mFabMenu.close(false);
        mFabMenu.hideMenuButton(false);
        if (savedInstanceState == null) {
            navigation.setSelectedItemId(navigation.getSelectedItemId());
        }

//            FragmentTransaction transaction =
//                    getSupportFragmentManager()
//                            .beginTransaction()
//                            .add(R.id.list_fragment_container, new ExpenseListFragment(), LIST_FRAGMENT);
//            if (mTwoPane) {
//                transaction.add(R.id.detail_fragment_container, new ExpenseDetailFragment(), DETAIL_FRAGMENT);
//            }
//            transaction.commit();
//        }
    }
//
//    @Override
//    boolean getDisplayHomeAsUpEnabled() {
//        return false;
//    }
//
//    @Override
//    int getContentViewWithToolbar() {
//        return R.layout.main_activity;
//    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        ListFragment listFragment;
        switch (item.getItemId()) {
            case R.id.cross_cover:
                listFragment = new CrossCoverListFragment();
                break;
            case R.id.expenses:
                listFragment = new ExpenseListFragment();
                break;
            default:
                return false;
        }
        Bundle args = new Bundle();
        args.putBoolean(ListFragment.IS_TWO_PANE, mTwoPane);
        listFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .replace(R.id.list_fragment_container, listFragment, LIST_FRAGMENT);
        if (mTwoPane) {
            DetailFragment detailFragment;
            switch (item.getItemId()) {
                case R.id.cross_cover:
                    detailFragment = new CrossCoverDetailFragment();
                    break;
                case R.id.expenses:
                    detailFragment = new ExpenseDetailFragment();
                    break;
                default:
                    return false;
            }
            transaction.replace(R.id.detail_fragment_container, detailFragment, DETAIL_FRAGMENT);
        }
        transaction.commit();
        return true;
    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        if (getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT) == null) {
            onNavigationItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(int itemType, long itemId) {
//        if (!mTwoPane) {
////            model.selectItem(expenseId);
////            ListFragment listFragment = (ListFragment) getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT);
////            if (listFragment != null) {
////                listFragment.
////            }
////        } else {
//        }
        if (!mTwoPane) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.ITEM_TYPE, itemType);
            intent.putExtra(DetailActivity.ITEM_ID, itemId);
            startActivity(intent);
        }
    }

    @Override
    public FloatingActionMenu getFloatingActionMenu() {
        return mFabMenu;
    }

    @Override
    public FloatingActionButton getFabNormalDay() {
        return mFabNormalDay;
    }

    @Override
    public FloatingActionButton getFabLongDay() {
        return mFabLongDay;
    }

    @Override
    public FloatingActionButton getFabNightShift() {
        return mFabNightShift;
    }
}
