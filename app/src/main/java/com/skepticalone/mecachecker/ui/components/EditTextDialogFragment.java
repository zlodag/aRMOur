package com.skepticalone.mecachecker.ui.components;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;

import com.skepticalone.mecachecker.R;

abstract public class EditTextDialogFragment extends AppCompatDialogFragment implements DialogInterface.OnClickListener {
    private static final String TAG = "EditTextDialogFragment";
    private static final String ITEM_ID = "ITEM_ID";
    private static final String TEXT = "TEXT";
    private static final String TITLE_ID = "TITLE_ID";
    private static final String HINT = "HINT";
    private EditText editText;
    private long mItemId;

    static Bundle getArgs(long id, @StringRes int title, @Nullable String text, @StringRes int hint) {
        Bundle args = new Bundle();
        args.putLong(ITEM_ID, id);
        args.putInt(TITLE_ID, title);
        args.putString(TEXT, text);
        args.putInt(HINT, hint);
        return args;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mItemId = getArguments().getLong(ITEM_ID);
        editText = (EditText) LayoutInflater.from(context).inflate(R.layout.edit_text, null, false);
        editText.setInputType(getInputType());
        editText.setText(getArguments().getString(TEXT));
        editText.setHint(getArguments().getInt(HINT));
    }

    abstract int getInputType();

    long getItemId() {
        return mItemId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(getArguments().getInt(TITLE_ID))
                .setPositiveButton(R.string.save, this)
                .setNegativeButton(R.string.cancel, this)
                .setView(editText)
                .create();
    }

    @Override
    public final void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            String trimmedText = editText.getText().toString().trim();
            save(trimmedText.length() == 0 ? null : trimmedText);
        }
    }

    abstract void save(@Nullable String trimmedTextWithLength);

}
