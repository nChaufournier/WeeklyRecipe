package com.nappingpirate.weeklyrecipe;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;

/**
 * Created by Nic on 10/2/2015.
 */
public class ShowMessage {


    public ShowMessage(String title,String message, Context context)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
