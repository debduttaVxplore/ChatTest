package com.coderusk.chattest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class Dbm {
    private static final String db_name = ChatDbm.db_name;
    private Context context;
    private Dbm(Context context) {
        this.context = context;
    }
    private void createTable(String table_defination)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        db.execSQL(table_defination);
        db.close();
    }
    private boolean tableExists(String name)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '"+name+"'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if(cursor!=null) {
                if(cursor.getCount()>0) {
                    db.close();
                    return true;
                }
            }
            db.close();
            return false;
        }
    }

    private void tableClean(String table)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        db.execSQL("delete from "+ table);
        db.close();
    }

    private void tableDrop(String table)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        db.execSQL("DROP TABLE IF EXISTS "+table);
        db.close();
    }
    ////////////////////////////

    public static Dbm with(Context context)
    {
        return new Dbm(context);
    }

    public void create(String definition)
    {
        createTable(definition);
    }

    public boolean exists(String table)
    {
        return tableExists(table);
    }

    public void clean(String table)
    {
        tableClean(table);
    }

    public void drop(String table)
    {
        tableDrop(table);
    }



    public long delete(String table,String where)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            long ret = db.delete(table, where, null);
            db.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return 0;
        }
    }

    public int queryResultcount(String query) {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            Cursor cursor = db.rawQuery( query, null );
            int ret = cursor.getCount();
            db.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return 0;
        }
    }

    public int executeQuery(String query)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        db.execSQL(query);
        String val = getString("select changes() as change_count","change_count");
        int ret = 0;
        try {
            ret = Integer.parseInt(val);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public int whereCount(String table,String where) {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            Cursor cursor = db.rawQuery( "select * from " + table + " where " + where, null );
            int ret = cursor.getCount();
            db.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return 0;
        }
    }

    public String getString(String query,String column) {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            Cursor cursor = db.rawQuery( query, null );
            int count = cursor.getCount();
            if(count!=1)
            {
                db.close();
                return "";
            }

            cursor.moveToFirst();
            String value = cursor.getString( cursor.getColumnIndex(column) );
            if(value==null)
            {
                db.close();
                return "";
            }
            db.close();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return "";
        }
    }

    public boolean getBoolean(String query,String column) {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            Cursor cursor = db.rawQuery( query, null );
            int count = cursor.getCount();
            if(count!=1)
            {
                db.close();
                return false;
            }

            cursor.moveToFirst();
            String value = cursor.getString( cursor.getColumnIndex(column) );
            if(value==null)
            {
                db.close();
                return false;
            }
            return !value.equalsIgnoreCase("0");
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return false;
        }
    }

    public long update(String table, ContentValues values,String where) {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            long ret = db.update(table, values, where, null);
            db.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return 0;
        }
    }

    public long insert(String table, ContentValues values) {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            long ret = db.insert(table, null, values);
            db.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return 0;
        }
    }

    public long upsert(String table, ContentValues values,String where) {
        int count = whereCount(table,where);
        if(count>0)
        {
            return update(table,values,where);
        }
        else
        {
            return insert(table,values);
        }
    }

    public interface ValueCaster<V>
    {
        V kast(String input);
    }

    public <K,V> HashMap<K,V> getHashMap(
            String query,
            String keyColumn,
            String valueColumn,
            Class<K> classOfKey,
            Class<V> classOfValue,
            ValueCaster<K> kValueCaster,
            ValueCaster<V> vValueCaster
            )
    {
        HashMap<K,V> hashMap = new HashMap<>();
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        try {
            Cursor cursor = db.rawQuery( query, null );
            int count = cursor.getCount();
            if(count<1)
            {
                db.close();
                return hashMap;
            }

            int keyIndex = cursor.getColumnIndex(keyColumn);
            int valueIndex = cursor.getColumnIndex(valueColumn);
            while (cursor.moveToNext()) {
                String key = cursor.getString(keyIndex);
                String value = cursor.getString(valueIndex);
                if(key!=null)
                {
                    if(value!=null)
                    {
                        K ckey = null;
                        V cvalue = null;
                        try {
                            if(kValueCaster!=null){ckey = kValueCaster.kast(key);}
                            else{ckey = classOfKey.cast(key);}

                            if(vValueCaster!=null){cvalue = vValueCaster.kast(value);}
                            else{cvalue = classOfValue.cast(value);}

                            hashMap.put(ckey,cvalue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            db.close();
            return hashMap;
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
            return hashMap;
        }
    }
    /////////////////////////////////////////////////
    public <T> T get(String query, Class<T> classOfT){
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        Cursor c = queryToCursor(db,query);
        if(c==null){return null;}
        int count = c.getCount();
        if(count!=1)
        {
            db.close();
            return null;
        }
        c.moveToFirst();
        JSONObject retVal = new JSONObject();
        for(int i=0; i<c.getColumnCount(); i++)
        {
            String cName = c.getColumnName(i);
            try
            {
                switch (c.getType(i)) {
                    case Cursor.FIELD_TYPE_INTEGER:
                        retVal.put(cName, c.getInt(i));
                        break;
                    case Cursor.FIELD_TYPE_FLOAT:
                        retVal.put(cName, c.getFloat(i));
                        break;
                    case Cursor.FIELD_TYPE_STRING:
                        retVal.put(cName, c.getString(i));
                        break;
                    default:
                        break;
                }

            }
            catch(Exception ex) {
                db.close();
                return null;
            }
        }
        if(retVal!=null)
        {
            T object = Utility.objectify(retVal.toString(),classOfT);
            return object;
        }
        return null;
    }

    public <T> ArrayList<T> getArray(String query, Class<T> classOfT){
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        ArrayList<T> arrayList = new ArrayList<>();
        Cursor c = queryToCursor(db,query);
        if(c==null){return null;}
        int count = c.getCount();
        if(count<1)
        {
            db.close();
            return null;
        }
        while (c.moveToNext())
        {
            JSONObject retVal = new JSONObject();
            for(int i=0; i<c.getColumnCount(); i++)
            {
                String cName = c.getColumnName(i);
                try
                {
                    switch (c.getType(i)) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            retVal.put(cName, c.getInt(i));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            retVal.put(cName, c.getFloat(i));
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            retVal.put(cName, c.getString(i));
                            break;
                        default:
                            break;
                    }

                }
                catch(Exception ex) {
                    db.close();
                    return null;
                }
            }
            if(retVal!=null)
            {
                T object = Utility.objectify(retVal.toString(),classOfT);
                arrayList.add(object);
            }
        }
        db.close();
        return arrayList;
    }

    private Cursor queryToCursor(SQLiteDatabase db,String query) {

        try {
            Cursor cursor = db.rawQuery( query, null );
            return cursor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String fieldCsv(String query, String field)
    {
        SQLiteDatabase db = context.openOrCreateDatabase(db_name,MODE_PRIVATE,null);
        Cursor c = queryToCursor(db,query);
        if(c==null)
        {
            db.close();
            return "";
        }
        int count = c.getCount();
        if(count<1)
        {
            db.close();
            return null;
        }
        int fieldIndex = c.getColumnIndex(field);
        if(fieldIndex<0)
        {
            db.close();
            return "";
        }
        ArrayList<String> values = new ArrayList<>();
        while (c.moveToNext())
        {
            String value = c.getString(fieldIndex);
            values.add(value);
        }
        db.close();
        return android.text.TextUtils.join(",", values);
    }
}
