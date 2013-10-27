package com.stronans.android.agenda.model;

public class Account2_2
{
    /**
     * The ID of the account
     */
    public static final String ID = "_id";

    /**
     * The URL to the calendar
     * <P>Type: TEXT (URL)</P>
     */
    public static final String URL = "url";

    /**
     * The name of the calendar
     * <P>Type: TEXT</P>
     */
    public static final String NAME = "name";

    /**
     * The display name of the calendar
     * <P>Type: TEXT</P>
     */
    public static final String DISPLAY_NAME = "displayName";

    /**
     * The location the of the events in the calendar
     * <P>Type: TEXT</P>
     */
    public static final String LOCATION = "location";

    /**
     * Should the calendar be hidden in the calendar selection panel?
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String HIDDEN = "hidden";

    /**
     * The owner account for this calendar, based on the calendar feed.
     * This will be different from the _SYNC_ACCOUNT for delegated calendars.
     * <P>Type: String</P>
     */
    public static final String OWNER_ACCOUNT = "ownerAccount";

    /**
     * Can the organizer respond to the event?  If no, the status of the
     * organizer should not be shown by the UI.  Defaults to 1
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String ORGANIZER_CAN_RESPOND = "organizerCanRespond";
    /**
     * The color of the calendar
     * <P>Type: INTEGER (color value)</P>
     */
    public static final String COLOR = "color";

    /**
     * The level of access that the user has for the calendar
     * <P>Type: INTEGER (one of the values below)</P>
     */
    public static final String ACCESS_LEVEL = "access_level";

    /** Cannot access the calendar */
    public static final int NO_ACCESS = 0;
    /** Can only see free/busy information about the calendar */
    public static final int FREEBUSY_ACCESS = 100;
    /** Can read all event details */
    public static final int READ_ACCESS = 200;
    public static final int RESPOND_ACCESS = 300;
    public static final int OVERRIDE_ACCESS = 400;
    /** Full access to modify the calendar, but not the access control settings */
    public static final int CONTRIBUTOR_ACCESS = 500;
    public static final int EDITOR_ACCESS = 600;
    /** Full access to the calendar */
    public static final int OWNER_ACCESS = 700;
    /** Domain admin */
    public static final int ROOT_ACCESS = 800;

    /**
     * Is the calendar selected to be displayed?
     * <P>Type: INTEGER (boolean)</P>
     */
    public static final String SELECTED = "selected";

    /**
     * The timezone the calendar's events occurs in
     * <P>Type: TEXT</P>
     */
    public static final String TIMEZONE = "timezone";

    /**
     * If this calendar is in the list of calendars that are selected for
     * syncing then "sync_events" is 1, otherwise 0.
     * <p>Type: INTEGER (boolean)</p>
     */
    public static final String SYNC_EVENTS = "sync_events";

    /**
     * Sync state data.
     * <p>Type: String (blob)</p>
     */
    public static final String SYNC_STATE = "sync_state";

    /**
     * The account that was used to sync the entry to the device.
     * <P>Type: TEXT</P>
     */
    public static final String _SYNC_ACCOUNT = "_sync_account";

    /**
     * The type of the account that was used to sync the entry to the device.
     * <P>Type: TEXT</P>
     */
    public static final String _SYNC_ACCOUNT_TYPE = "_sync_account_type";

    /**
     * The unique ID for a row assigned by the sync source. NULL if the row has never been synced.
     * <P>Type: TEXT</P>
     */
    public static final String _SYNC_ID = "_sync_id";

    /**
     * The last time, from the sync source's point of view, that this row has been synchronized.
     * <P>Type: INTEGER (long)</P>
     */
    public static final String _SYNC_TIME = "_sync_time";

    /**
     * The version of the row, as assigned by the server.
     * <P>Type: TEXT</P>
     */
    public static final String _SYNC_VERSION = "_sync_version";

    /**
     * For use by sync adapter at its discretion; not modified by CalendarProvider
     * Note that this column was formerly named _SYNC_LOCAL_ID.  We are using it to avoid a
     * schema change.
     * TODO Replace this with something more general in the future.
     * <P>Type: INTEGER (long)</P>
     */
    public static final String _SYNC_DATA = "_sync_local_id";

    /**
     * Used only in persistent providers, and only during merging.
     * <P>Type: INTEGER (long)</P>
     */
    public static final String _SYNC_MARK = "_sync_mark";

    /**
     * Used to indicate that local, unsynced, changes are present.
     * <P>Type: INTEGER (long)</P>
     */
    public static final String _SYNC_DIRTY = "_sync_dirty";

    /**
     * The name of the account instance to which this row belongs, which when paired with
     * {@link #ACCOUNT_TYPE} identifies a specific account.
     * <P>Type: TEXT</P>
     */
    public static final String ACCOUNT_NAME = "account_name";

    /**
     * The type of account to which this row belongs, which when paired with
     * {@link #ACCOUNT_NAME} identifies a specific account.
     * <P>Type: TEXT</P>
     */
    public static final String ACCOUNT_TYPE = "account_type";

    
    int Id;
    String AccountName;
    String AccountType;
    String Name;
    String CalendarDisplayName;
    int calendarColour;
    String calendarAccessLevel;
    String OwnerAccount;

    /**
     * @return the id
     */
    public int getId()
    {
        return Id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id)
    {
        Id = id;
    }
    /**
     * @return the accountName
     */
    public String getAccountName()
    {
        if(AccountName == null)
            return "NULL";
        else
            return AccountName;
    }
    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName)
    {
        AccountName = accountName;
    }
    /**
     * @return the accountType
     */
    public String getAccountType()
    {
        if(AccountType == null)
            return "NULL";
        else
            return AccountType;
    }
    /**
     * @param accountType the accountType to set
     */
    public void setAccountType(String accountType)
    {
        AccountType = accountType;
    }
    /**
     * @return the name
     */
    public String getName()
    {
        if(Name == null)
            return "NULL";
        else
            return Name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        Name = name;
    }
    /**
     * @return the calendarDisplayName
     */
    public String getCalendarDisplayName()
    {
        if(CalendarDisplayName == null)
            return "NULL";
        else
            return CalendarDisplayName;
    }
    /**
     * @param calendarDisplayName the calendarDisplayName to set
     */
    public void setCalendarDisplayName(String calendarDisplayName)
    {
        CalendarDisplayName = calendarDisplayName;
    }
    /**
     * @return the calendarColour
     */
    public int getCalendarColour()
    {
        return calendarColour;
    }
    /**
     * @param calendarColour the calendarColour to set
     */
    public void setCalendarColour(int calendarColour)
    {
        this.calendarColour = calendarColour;
    }
    /**
     * @return the calendarAccessLevel
     */
    public String getCalendarAccessLevel()
    {
        if(calendarAccessLevel == null)
            return "NULL";
        else
            return calendarAccessLevel;
    }
    /**
     * @param calendarAccessLevel the calendarAccessLevel to set
     */
    public void setCalendarAccessLevel(String calendarAccessLevel)
    {
        this.calendarAccessLevel = calendarAccessLevel;
    }
    /**
     * @return the ownerAccount
     */
    public String getOwnerAccount()
    {
        if(OwnerAccount == null)
            return "NULL";
        else
            return OwnerAccount;
    }
    /**
     * @param ownerAccount the ownerAccount to set
     */
    public void setOwnerAccount(String ownerAccount)
    {
        OwnerAccount = ownerAccount;
    }
}
