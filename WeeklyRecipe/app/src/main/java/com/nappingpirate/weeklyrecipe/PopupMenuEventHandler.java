package com.nappingpirate.weeklyrecipe;

import android.content.Context;
import android.view.MenuItem;
import android.widget.PopupMenu;

/**
 * Created by Nic on 10/29/2015.
 */
public class PopupMenuEventHandler implements PopupMenu.OnMenuItemClickListener{
    Context context;

    public PopupMenuEventHandler(Context context) {
        this.context = context;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        return false;
    }
}
