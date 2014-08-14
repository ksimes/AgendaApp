/**
 * 
 */
package com.stronans.android.agenda.model;

/**
 * Very simple immutable POJO originally designed to hold two parts of information for presentation on the about display list
 * box.
 * 
 * @author SimonKing
 */
public class AboutData
{
    String key;
    String value;

    public AboutData(String key, String value)
    {
        this.key = key;
        this.value = value;
    }

    public String key()
    {
        return key;
    }

    public String value()
    {
        return value;
    }
}
