package com.skepticalone.mecachecker.components.shifts;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.skepticalone.mecachecker.R;

public class CommentDialogFragment extends EditTextDialogFragment {

    private static final String CONTENT_URI = "CONTENT_URI";
    private static final String COLUMN_NAME = "COLUMN_NAME";

    public static CommentDialogFragment newInstance(@Nullable String comment, @NonNull Uri contentUri, @NonNull String columnName) {
        CommentDialogFragment fragment = new CommentDialogFragment();
        Bundle args = getArgs(R.string.comment, R.layout.input_comment, comment);
        args.putParcelable(CONTENT_URI, contentUri);
        args.putString(COLUMN_NAME, columnName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    void save() {
        ContentValues values = new ContentValues();
        values.put(getArguments().getString(COLUMN_NAME), getText());
        Uri contentUri = getArguments().getParcelable(CONTENT_URI);
        assert contentUri != null;
        getActivity().getContentResolver().update(contentUri, values, null, null);
    }
}
