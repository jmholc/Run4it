package com.example.adm.pantallas;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by adm on 14/06/2017.
 */

public class Intromanager {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    public Intromanager(Context context)
    {
        this.context=context;
        pref=context.getSharedPreferences("first",0);
        editor=pref.edit();
    }
    public void setFirst(boolean isFirst){
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public boolean check(){
        return pref.getBoolean("check",true);
    }
}
