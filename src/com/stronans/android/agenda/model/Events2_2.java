package com.stronans.android.agenda.model;

public interface Events2_2
{
    /**
     * The calendar the event belongs to
     * <P>Type: INTEGER (foreign key to the Calendars table)</P>
     */
    public static final String CALENDAR_ID = "calendar_id";

    /**
     * The URI for an HTML version of this event.
     * <P>Type: TEXT</P>
     */
    public static final String HTML_URI = "htmlUri";

    /**
     * The title of the event
     * <P>Type: TEXT</P>
     */
    public static final String TITLE = "title";

    /**
     * The description of the event
     * <P>Type: TEXT</P>
     */
    public static final String DESCRIPTION = "description";

    /**
     * Where the event takes place.
     * <P>Type: TEXT</P>
     */
    public static final String EVENT_LOCATION = "eventLocation";

    /**
     * The event status
     * <P>Type: INTEGER (int)</P>
     */
    public static final String STATUS = "eventStatus";

    public static final int STATUS_TENTATIVE = 0;
    public static final int STATUS_CONFIRMED = 1;
    public static final int STATUS_CANCELED = 2;

    /**
     * This is a copy of the attendee status for the owner of this event.
     * This field is copied here so that we can efficiently filter out
     * events that are declined without having to look in the Attendees
     * table.
     *
     * <P>Type: INTEGER (int)</P>
     */
    public static final String SELF_ATTENDEE_STATUS = "selfAttendeeStatus";

    /**
     * This column is available for use by sync adapters
     * <P>Type: TEXT</P>
     */
    public static final String SYNC_ADAPTER_DATA = "syncAdapterData";

    /**
     * The comments feed uri.
     * <P>Type: TEXT</P>
     */
    public static final String COMMENTS_URI = "commentsUri";

    /**
     * The time the event starts
     * <P>Type: INTEGER (long; millis since epoch)</P>
     */
    public static final String DTSTART = "dtstart";

    /**
     * The time the event ends
     * <P>Type: INTEGER (long; millis since epoch)</P>
     */
    public static final String DTEND = "dtend";

    /**
     * The duration of the event
     * <P>Type: TEXT (duration in RFC2445 format)</P>
     */
    public static final String DURATION = "duration";

    /**
     * The timezone for the event.
     * <P>Type: TEXT
     */
    public static final String EVENT_TIMEZONE = "eventTimezone";

    /**
     * Whether the event lasts all day or not
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String ALL_DAY = "allDay";

    /**
     * Visibility for the event.
     * <P>Type: INTEGER</P>
     */
    public static final String VISIBILITY = "visibility";

    public static final int VISIBILITY_DEFAULT = 0;
    public static final int VISIBILITY_CONFIDENTIAL = 1;
    public static final int VISIBILITY_PRIVATE = 2;
    public static final int VISIBILITY_PUBLIC = 3;

    /**
     * Transparency for the event -- does the event consume time on the calendar?
     * <P>Type: INTEGER</P>
     */
    public static final String TRANSPARENCY = "transparency";

    public static final int TRANSPARENCY_OPAQUE = 0;

    public static final int TRANSPARENCY_TRANSPARENT = 1;

    /**
     * Whether the event has an alarm or not
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String HAS_ALARM = "hasAlarm";

    /**
     * Whether the event has extended properties or not
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String HAS_EXTENDED_PROPERTIES = "hasExtendedProperties";

    /**
     * The recurrence rule for the event.
     * than one.
     * <P>Type: TEXT</P>
     */
    public static final String RRULE = "rrule";

    /**
     * The recurrence dates for the event.
     * <P>Type: TEXT</P>
     */
    public static final String RDATE = "rdate";

    /**
     * The recurrence exception rule for the event.
     * <P>Type: TEXT</P>
     */
    public static final String EXRULE = "exrule";

    /**
     * The recurrence exception dates for the event.
     * <P>Type: TEXT</P>
     */
    public static final String EXDATE = "exdate";

    /**
     * The _sync_id of the original recurring event for which this event is
     * an exception.
     * <P>Type: TEXT</P>
     */
    public static final String ORIGINAL_EVENT = "originalEvent";

    /**
     * The original instance time of the recurring event for which this
     * event is an exception.
     * <P>Type: INTEGER (long; millis since epoch)</P>
     */
    public static final String ORIGINAL_INSTANCE_TIME = "originalInstanceTime";

    /**
     * The allDay status (true or false) of the original recurring event
     * for which this event is an exception.
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String ORIGINAL_ALL_DAY = "originalAllDay";

    /**
     * The last date this event repeats on, or NULL if it never ends
     * <P>Type: INTEGER (long; millis since epoch)</P>
     */
    public static final String LAST_DATE = "lastDate";

    /**
     * Whether the event has attendee information.  True if the event
     * has full attendee data, false if the event has information about
     * self only.
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String HAS_ATTENDEE_DATA = "hasAttendeeData";

    /**
     * Whether guests can modify the event.
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String GUESTS_CAN_MODIFY = "guestsCanModify";

    /**
     * Whether guests can invite other guests.
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String GUESTS_CAN_INVITE_OTHERS = "guestsCanInviteOthers";

    /**
     * Whether guests can see the list of attendees.
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String GUESTS_CAN_SEE_GUESTS = "guestsCanSeeGuests";

    /**
     * Email of the organizer (owner) of the event.
     * <P>Type: STRING</P>
     */
    public static final String ORGANIZER = "organizer";

    /**
     * Whether the user can invite others to the event.
     * The GUESTS_CAN_INVITE_OTHERS is a setting that applies to an arbitrary guest,
     * while CAN_INVITE_OTHERS indicates if the user can invite others (either through
     * GUESTS_CAN_INVITE_OTHERS or because the user has modify access to the event).
     * <P>Type: INTEGER (boolean, readonly)</P>
     */
    public static final String CAN_INVITE_OTHERS = "canInviteOthers";

    /**
     * The owner account for this calendar, based on the calendar (foreign
     * key into the calendars table).
     * <P>Type: String</P>
     */
    public static final String OWNER_ACCOUNT = "ownerAccount";

    /**
     * Whether the row has been deleted.  A deleted row should be ignored.
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String DELETED = "deleted";
}
