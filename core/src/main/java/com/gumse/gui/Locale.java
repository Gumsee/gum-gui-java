package com.gumse.gui;

import java.util.HashMap;
import java.util.Map;

public class Locale 
{
    private static Locale pCurrentLocale;
    private String sShortForm, sLanguage;
    private Map<String, String> mStrings;


    public Locale(String language, String shortform)
    {
        this.sLanguage = language;
        this.sShortForm = shortform;
        this.mStrings = new HashMap<>();
    }

    //
    // Setter
    //
    public void setString(String id, String str)       { mStrings.put(id, str); }
    public static void setCurrentLocale(Locale locale) { pCurrentLocale = locale; }


    //
    // Getter
    //
    public String getString(String id)
    {
        String ret = mStrings.get(id);
        if(ret == null)
            return "";
        return ret;
    }
    public String getShortForm()            { return sShortForm; }
    public String getLanguage()             { return sLanguage; }
    public static Locale getCurrentLocale() { return pCurrentLocale; }
}