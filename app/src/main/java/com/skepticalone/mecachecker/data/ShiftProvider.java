package com.skepticalone.mecachecker.data;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

public final class ShiftProvider extends ContentProvider {

    public static final String
            START_TIME = ShiftContract.Shift.COLUMN_NAME_START,
            END_TIME = ShiftContract.Shift.COLUMN_NAME_END,
            FORMATTED_DATE = ShiftContract.Shift.START_AS_DATE,
            FORMATTED_START_TIME = ShiftContract.Shift.START_AS_TIME,
            FORMATTED_END_TIME = ShiftContract.Shift.END_AS_TIME;
    private static final String WITH_COMPLIANCE = "_with_compliance";
    private static final String TAG = "ShiftProvider";
    private static final String SHIFT_OVERLAP_TOAST_MESSAGE = "Shift overlaps!";
    private static final String
            AUTHORITY = "com.skepticalone.mecachecker.provider",
            PROVIDER_TYPE = "/vnd.com.skepticalone.provider.";
    private static final Uri baseContentUri = new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority(AUTHORITY).build();
    public static final Uri shiftsUri = baseContentUri.buildUpon().appendPath(ShiftContract.Shift.TABLE_NAME).build();
    public static final Uri shiftsWithComplianceUri = baseContentUri.buildUpon().appendPath(ShiftContract.Shift.TABLE_NAME + WITH_COMPLIANCE).build();
    //    public static final Uri lastShiftUri = Uri.withAppendedPath(shiftsUri, "last");
    private static final String
            SHIFT_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + PROVIDER_TYPE + ShiftContract.Shift.TABLE_NAME,
            SHIFTS_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + PROVIDER_TYPE + ShiftContract.Shift.TABLE_NAME;
    private static final int SHIFTS = 1;
    private static final int SHIFT_ID = 2;
    private static final int SHIFTS_WITH_COMPLIANCE = 3;
    private static final int SHIFT_ID_WITH_COMPLIANCE = 4;
    //    private static final int LAST_SHIFT = 3;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME, SHIFTS);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME + "/#", SHIFT_ID);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME + WITH_COMPLIANCE, SHIFTS_WITH_COMPLIANCE);
        sUriMatcher.addURI(AUTHORITY, ShiftContract.Shift.TABLE_NAME + WITH_COMPLIANCE + "/#", SHIFT_ID_WITH_COMPLIANCE);
    }

    private ShiftDbHelper mDbHelper;

    public static Uri shiftUri(long shiftId) {
        return Uri.withAppendedPath(shiftsUri, Long.toString(shiftId));
    }

    public static Uri shiftWithComplianceUri(long shiftId) {
        return Uri.withAppendedPath(shiftsWithComplianceUri, Long.toString(shiftId));
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new ShiftDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case SHIFTS_WITH_COMPLIANCE:
            case SHIFT_ID_WITH_COMPLIANCE:
                projection = ComplianceCursor.PROJECTION;
                selection = null;
                selectionArgs = null;
                // intentional fall-through
            case SHIFTS:
                sortOrder = ShiftContract.Shift.COLUMN_NAME_START;
                break;
            case SHIFT_ID:
                selection = ShiftContract.Shift._ID + "=" + uri.getLastPathSegment();
                selectionArgs = null;
                sortOrder = null;
                break;
//            case LAST_SHIFT:
//                sortOrder = ShiftContract.Shift.COLUMN_NAME_START + " DESC";
//                limit = "1";
//                break;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
        Cursor cursor = mDbHelper.getReadableDatabase().query(ShiftContract.Shift.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        if (match == SHIFTS_WITH_COMPLIANCE) {
            cursor = new ComplianceCursor(cursor, null);
        } else if (match == SHIFT_ID_WITH_COMPLIANCE) {
            cursor = new ComplianceCursor(cursor, Long.valueOf(uri.getLastPathSegment()));
        }
        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
            case SHIFTS_WITH_COMPLIANCE:
                return SHIFTS_TYPE;
            case SHIFT_ID:
//            case LAST_SHIFT:
                return SHIFT_TYPE;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case SHIFTS:
                try {
                    long shiftId = mDbHelper.getWritableDatabase().insertOrThrow(ShiftContract.Shift.TABLE_NAME, null, values);
                    Context context = getContext();
                    if (context != null) {
                        ContentResolver contentResolver = context.getContentResolver();
                        contentResolver.notifyChange(uri, null);
                        contentResolver.notifyChange(shiftsWithComplianceUri, null);
                    }
                    return shiftUri(shiftId);
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(getContext(), SHIFT_OVERLAP_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to insert new shift: ", e);
                    return null;
                }
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                int deleted = mDbHelper.getWritableDatabase().delete(ShiftContract.Shift.TABLE_NAME, ShiftContract.Shift._ID + "=" + uri.getLastPathSegment(), null);
                if (deleted > 0) {
                    Context context = getContext();
                    if (context != null) {
                        ContentResolver contentResolver = context.getContentResolver();
                        contentResolver.notifyChange(uri, null);
                        contentResolver.notifyChange(shiftsWithComplianceUri, null);
                    }
                }
                return deleted;
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case SHIFT_ID:
                try {
                    int updated = mDbHelper.getWritableDatabase().update(ShiftContract.Shift.TABLE_NAME, values, ShiftContract.Shift._ID + "=" + uri.getLastPathSegment(), null);
                    if (updated > 0) {
                        Context context = getContext();
                        if (context != null) {
                            ContentResolver contentResolver = context.getContentResolver();
                            contentResolver.notifyChange(uri, null);
                            contentResolver.notifyChange(shiftsWithComplianceUri, null);
                        }
                    }
                    return updated;
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(getContext(), SHIFT_OVERLAP_TOAST_MESSAGE, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to update shift: ", e);
                    return 0;
                }
            default:
                throw new IllegalArgumentException("Invalid Uri: " + uri);
        }
    }
}
