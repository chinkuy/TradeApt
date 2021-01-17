package com.tradeapt.DB;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AptDB extends SQLiteOpenHelper {

    final static String TableName = "aptTable";

    private ArrayList<String> AptName;

    public AptDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        AptName = new ArrayList<>();
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

    public void insertData(SQLiteDatabase db, String aptName, AptList aptList){

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
