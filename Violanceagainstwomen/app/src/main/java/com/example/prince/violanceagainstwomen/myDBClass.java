package com.example.prince.violanceagainstwomen;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nipu on 12/25/2015.
 */
public class myDBClass extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mydatabase";
    private static final String TABLE_MEMBER = "members";

    public myDBClass(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_MEMBER + "(MemberID INTEGER PRIMARY KEY AUTOINCREMENT," + " Name TEXT(100)," +"Phone Text(100));");
        Log.d("CREATE TABLE", "Create Table Successfully.");
    }

    // Insert Data

    public long InsertData(String strMemberID, String strName,String strPhone) {
        try {
            SQLiteDatabase db;

            db = this.getWritableDatabase(); // Write Data
            ContentValues Val = new ContentValues();
            Val.put("MemberID", strMemberID);
            Val.put("Name", strName);

            Val.put("Phone",strPhone);


            long rows = db.insert(TABLE_MEMBER, null, Val);
            db.close();

            return rows; // return rows inserted.
        } catch (Exception e) {
            return -1;
        }

    }

    // Select Data
    public String[] SelectData(String strMemberID) {

        try {

            String arrData[] = null;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            Cursor cursor = db.query(TABLE_MEMBER, new String[]{"*"},
                    "MemberID=?",

                    new String[]{String.valueOf(strMemberID)}, null, null, null, null);
            if(cursor != null)

            {
                if (cursor.moveToFirst()) {

                    arrData = new String[cursor.getColumnCount()];

                    arrData[0] = cursor.getString(0);
                    arrData[1] = cursor.getString(1);
                    arrData[2] = cursor.getString(2);


                }

            }

            cursor.close();
            db.close();

            return arrData;

        } catch (Exception e) {
            return null;

        }

    }

    // Show All Data
    public ArrayList<HashMap<String, String>> SelectAllData() {
        try {
            ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();

            HashMap<String, String> map;
            SQLiteDatabase db;
            db = this.getReadableDatabase(); // Read Data
            String strSQL = "SELECT  * FROM " + TABLE_MEMBER;
            Cursor cursor = db.rawQuery(strSQL, null);

            if(cursor != null)
            {
                if (cursor.moveToFirst()) {

                    do {
                        map = new HashMap<String, String>();

                        map.put("MemberID", cursor.getString(0));

                        map.put("Name", cursor.getString(1));



                        map.put("Phone", cursor.getString(2));



                        MyArrList.add(map);
                    } while (cursor.moveToNext());

                }

            }

            cursor.close();

            db.close();
            return MyArrList;

        } catch (Exception e) {
            return null;

        }

    }


    // Update Data
    public long UpdateData(String strMemberID,String strName,String strPhone) {

        try {

            SQLiteDatabase db;

            db = this.getWritableDatabase(); // Write Data

            ContentValues Val = new ContentValues();
            Val.put("Name", strName);
            Val.put("Phone", strPhone);


            long rows = db.update(TABLE_MEMBER, Val, " MemberID = ?",
                    new String[] { String.valueOf(strMemberID) });
            db.close();

            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }

    }

    // Delete Data
    public long DeleteData(String strMemberID) {

        try {

            SQLiteDatabase db;

            db = this.getWritableDatabase(); // Write Data


            long rows = db.delete(TABLE_MEMBER, "MemberID = ?",
                    new String[]{String.valueOf(strMemberID)});



            db.close();

            return rows; // return rows deleted.

        } catch (Exception e) {

            return -1;
        }

    }






    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBER);
        // Re Create on method  onCreate
        onCreate(db);
    }

}
