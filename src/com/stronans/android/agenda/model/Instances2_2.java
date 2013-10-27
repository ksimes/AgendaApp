package com.stronans.android.agenda.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * Contains one entry per calendar event instance. Recurring events show up every time
 * they occur.
 */
public final class Instances2_2 implements BaseColumns {
    public static final String AUTHORITY = "com.android.calendar";

    private static final String WHERE_CALENDARS_SELECTED = Account2_2.SELECTED + "=1";

    public static final Cursor query(ContentResolver cr, String[] projection,
                                     long begin, long end) {
        Uri.Builder builder = CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, begin);
        ContentUris.appendId(builder, end);
        return cr.query(builder.build(), projection, WHERE_CALENDARS_SELECTED,
                     null, DEFAULT_SORT_ORDER);
    }

    public static final Cursor query(ContentResolver cr, String[] projection,
                                     long begin, long end, String where, String orderBy) {
        Uri.Builder builder = CONTENT_URI.buildUpon();
        ContentUris.appendId(builder, begin);
        ContentUris.appendId(builder, end);
        if (TextUtils.isEmpty(where)) {
            where = WHERE_CALENDARS_SELECTED;
        } else {
            where = "(" + where + ") AND " + WHERE_CALENDARS_SELECTED;
        }
        return cr.query(builder.build(), projection, where,
                     null, orderBy == null ? DEFAULT_SORT_ORDER : orderBy);
    }

    /**
     * The content:// style URL for this table
     */
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY +
            "/instances/when");
    public static final Uri CONTENT_BY_DAY_URI =
        Uri.parse("content://" + AUTHORITY + "/instances/whenbyday");

    /**
     * The default sort order for this table.
     */
    public static final String DEFAULT_SORT_ORDER = "begin ASC";

    /**
     * The sort order is: events with an earlier start time occur
     * first and if the start times are the same, then events with
     * a later end time occur first. The later end time is ordered
     * first so that long-running events in the calendar views appear
     * first.  If the start and end times of two events are
     * the same then we sort alphabetically on the title.  This isn't
     * required for correctness, it just adds a nice touch.
     */
    public static final String SORT_CALENDAR_VIEW = "begin ASC, end DESC, title ASC";

    /**
     * The beginning time of the instance, in UTC milliseconds
     * <P>Type: INTEGER (long; millis since epoch)</P>
     */
    public static final String BEGIN = "begin";

    /**
     * The ending time of the instance, in UTC milliseconds
     * <P>Type: INTEGER (long; millis since epoch)</P>
     */
    public static final String END = "end";

    /**
     * The event for this instance
     * <P>Type: INTEGER (long, foreign key to the Events table)</P>
     */
    public static final String EVENT_ID = "event_id";

    /**
     * The Julian start day of the instance, relative to the local timezone
     * <P>Type: INTEGER (int)</P>
     */
    public static final String START_DAY = "startDay";

    /**
     * The Julian end day of the instance, relative to the local timezone
     * <P>Type: INTEGER (int)</P>
     */
    public static final String END_DAY = "endDay";

    /**
     * The start minute of the instance measured from midnight in the
     * local timezone.
     * <P>Type: INTEGER (int)</P>
     */
    public static final String START_MINUTE = "startMinute";

    /**
     * The end minute of the instance measured from midnight in the
     * local timezone.
     * <P>Type: INTEGER (int)</P>
     */
    public static final String END_MINUTE = "endMinute";
}
