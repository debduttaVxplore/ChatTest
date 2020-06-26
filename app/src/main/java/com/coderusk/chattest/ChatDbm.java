package com.coderusk.chattest;

import android.content.ContentValues;
import android.content.Context;

class ChatDbm {
    public static final String db_name = "com_coderusk_chat_database";
    public static final String table_name = "chat_table";

    public static String definition()
    {
        return Tefinition
                .create()
                .table()
                .name(table_name)
                .column("id").INTEGER().PRIMARY_KEY().AUTOINCREMENT()
                .column("value").VARCHAR().NOT_NULL()
                .queryString();
    }

    public static void assureTableExist(Context context)
    {
        Dbm dbm = Dbm.with(context);
        if(!dbm.exists(table_name))
        {
            dbm.create(definition());
        }
    }

    public static boolean set(Context context, String value)
    {
        ContentValues cv = new ContentValues();
        cv.put("value",value);
        assureTableExist(context);
        return Dbm.with(context).insert(table_name,cv)>0;
    }

    public static String getLast(Context context)
    {
        assureTableExist(context);
        String value = Dbm.with(context).getString("SELECT value FROM " +
                table_name +
                " WHERE id = (SELECT MAX(ID) FROM " +
                table_name +
                ")","value");
        return value;
    }
}
