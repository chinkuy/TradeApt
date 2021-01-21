package com.tradeapt.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class AptDB extends SQLiteOpenHelper {

    final static String TableName = "aptTable";
    Context mContext;

    private ArrayList<String> AptName;

    public AptDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        AptName = new ArrayList<>();
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists "+TableName+" ("
                + "_id integer primary key autoincrement,"
                + "aptName text);";

        Log.d("Hey","TableName : " + TableName );
        db.execSQL(sql);
        getData(db);
    }

    public void createTable(SQLiteDatabase db, String aptName){

        String sql = "CREATE TABLE IF NOT EXISTS " + aptName + "("
                + "_id integer primary key autoincrement, "
                + "AptPrice text NOT NULL, "
                + "AptExclusiveUse text NOT NULL, "
                + "AptFloor text NOT NULL, "
                + "AptDateMonth text NOT NULL, "
                + "AptDateDay text NOT NULL);";

        db.execSQL(sql);
    }

    public void insertData(SQLiteDatabase db, String aptName, Apt aptList){

        String sql = "INSERT INTO " + aptName + " ("
                + "AptPrice, AptExclusiveUse, AptFloor, AptDateMonth, AptDateDay)"
                + "VALUES ("
                + "'" + aptList.getAptPrice() + "',"
                + "'" + aptList.getAptExclusiveUse() + "',"
                + "'" + aptList.getAptFloor() + "',"
                + "'" + aptList.getAptDateMonth() + "',"
                + "'" + aptList.getAptDateDay() + "');";

        db.execSQL(sql);
    }



    public ArrayList<String> getTableList(SQLiteDatabase db){

        ArrayList<String> aptList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (cursor.moveToFirst()) {
            while ( !cursor.isAfterLast() ) {

                if(cursor.getString(0).equals("android_metadata") ||
                   cursor.getString(0).equals("aptTable") ||
                   cursor.getString(0).equals("sqlite_sequence")) {
                }else {
                    aptList.add(cursor.getString(0));
                }
                cursor.moveToNext();
            }
        }

        return aptList;
    }

    public HashMap<String,ArrayList<Apt>> getAptInfo(SQLiteDatabase db, ArrayList<String> aptNameList){

        HashMap<String,ArrayList<Apt>> aptMap = new HashMap<>();

        for(int i = 0 ; i < aptNameList.size();i++) {

            ArrayList<Apt> aptLists = new ArrayList<>();
            Apt apt = new Apt();

            String sql = "SELECT _id, AptPrice, AptExclusiveUse, AptFloor, AptDateMonth, AptDateDay FROM " + aptNameList.get(i);
            Cursor cursor = db.rawQuery(sql, null);

            while (cursor.moveToNext()) {
                apt.setAptPrice(cursor.getString(1));
                apt.setAptExclusiveUse(cursor.getString(2));
                apt.setAptFloor(cursor.getString(3));
                apt.setAptDateMonth(cursor.getString(4));
                apt.setAptDateDay(cursor.getString(5));
                aptLists.add(apt);
            }

            aptMap.put(aptNameList.get(i), aptLists);
            cursor.close();
        }

        return aptMap;
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE if exists "+TableName;

        db.execSQL(sql);
        onCreate(db);
    }

    private void getData(SQLiteDatabase db){

        Cursor cursor = db.query(TableName, null,null,null,null,null,null) ;

        while (cursor.moveToNext()) {
            //AptName.add(cursor.getString(0));
            AptName.add(cursor.getString(1));
        }
    }

    public ArrayList<String> getAptNameList(){

        return AptName;
    }
}
