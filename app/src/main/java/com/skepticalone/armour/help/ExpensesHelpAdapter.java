package com.skepticalone.armour.help;

import android.support.v7.widget.RecyclerView;

import com.skepticalone.armour.R;

final class ExpensesHelpAdapter extends HelpAdapter {

    @Override
    public int getItemViewType(int position) {
        switch (POSITIONS.values()[position]) {
            case PAYMENT_TITLE:
            case ADD_TITLE:
            case DELETE_TITLE:
            case TOTALS_TITLE:
            case SUBTOTALS_TITLE:
                return ITEM_TYPE_TITLE;
            case PAYMENT_UNCLAIMED:
            case PAYMENT_CLAIMED:
            case PAYMENT_PAID:
            case DELETE_SELECT:
            case DELETE_DELETE:
            case TOTALS_TOTALS:
            case SUBTOTALS_SELECT:
            case SUBTOTALS_SUBTOTALS:
                return ITEM_TYPE_LABEL_PLAIN;
            case ADD_EXPENSE:
                return ITEM_TYPE_LABEL_FAB;
            case PAYMENT_DESCRIPTION_1:
            case PAYMENT_DESCRIPTION_2:
            case TOTALS_DESCRIPTION:
            case SUBTOTALS_DESCRIPTION:
                return ITEM_TYPE_DESCRIPTION;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (POSITIONS.values()[position]) {
            case PAYMENT_TITLE:
                ((TextViewHolder) holder).bind(R.string.payment);
                break;
            case PAYMENT_UNCLAIMED:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_unclaimed_black_24dp, R.string.unclaimed);
                break;
            case PAYMENT_CLAIMED:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_claimed_black_24dp, R.string.claimed);
                break;
            case PAYMENT_PAID:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_paid_black_24dp, R.string.paid);
                break;
            case PAYMENT_DESCRIPTION_1:
                ((TextViewHolder) holder).bind(R.string.help_payment_description);
                break;
            case PAYMENT_DESCRIPTION_2:
                ((TextViewHolder) holder).bind(R.string.help_payment_expense_description);
                break;
            case ADD_TITLE:
                ((TextViewHolder) holder).bind(R.string.new_expense);
                break;
            case ADD_EXPENSE:
                ((LabelViewHolder.Fab) holder).bind(R.drawable.ic_add_white_24dp, R.string.help_add_expense_label);
                break;
            case DELETE_TITLE:
                ((TextViewHolder) holder).bind(R.string.help_delete_expenses_title);
                break;
            case DELETE_SELECT:
            case SUBTOTALS_SELECT:
                ((LabelViewHolder.Plain) holder).bind(false, R.drawable.ic_check_circle_red_24dp, R.string.help_select_expenses_label);
                break;
            case DELETE_DELETE:
                ((LabelViewHolder.Plain) holder).bind(true, R.drawable.ic_delete_white_24dp, R.string.help_delete_delete_label);
                break;
            case TOTALS_TITLE:
                ((TextViewHolder) holder).bind(R.string.help_totals_title);
                break;
            case TOTALS_TOTALS:
                ((LabelViewHolder.Plain) holder).bind(true, R.drawable.ic_sigma_white_24dp, R.string.help_totals_totals_label);
                break;
            case TOTALS_DESCRIPTION:
                ((TextViewHolder) holder).bind(R.string.help_totals_expenses_description);
                break;
            case SUBTOTALS_TITLE:
                ((TextViewHolder) holder).bind(R.string.help_subtotals_expenses_title);
                break;
            case SUBTOTALS_SUBTOTALS:
                ((LabelViewHolder.Plain) holder).bind(true, R.drawable.ic_sigma_white_24dp, R.string.help_subtotals_subtotals_label);
                break;
            case SUBTOTALS_DESCRIPTION:
                ((TextViewHolder) holder).bind(R.string.help_subtotals_expenses_description);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public boolean shouldHaveDividerBelow(int position) {
        switch (POSITIONS.values()[position]) {
            case PAYMENT_DESCRIPTION_2:
            case ADD_EXPENSE:
            case DELETE_DELETE:
            case TOTALS_DESCRIPTION:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getItemCount() {
        return POSITIONS.values().length;
    }

    private enum POSITIONS {

        PAYMENT_TITLE,
        PAYMENT_UNCLAIMED,
        PAYMENT_CLAIMED,
        PAYMENT_PAID,
        PAYMENT_DESCRIPTION_1,
        PAYMENT_DESCRIPTION_2,

        ADD_TITLE,
        ADD_EXPENSE,

        DELETE_TITLE,
        DELETE_SELECT,
        DELETE_DELETE,

        TOTALS_TITLE,
        TOTALS_TOTALS,
        TOTALS_DESCRIPTION,

        SUBTOTALS_TITLE,
        SUBTOTALS_SELECT,
        SUBTOTALS_SUBTOTALS,
        SUBTOTALS_DESCRIPTION,

    }

}
