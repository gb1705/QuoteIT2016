package com.gb.quoteit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

/**

 * Created by GB on 1/21/16.
 */
public class DBHandler extends SQLiteOpenHelper {

    public static String DB_NAME = "QUOTE.sqlite";
    public static String TABLE_NAME = "TBQUOTE";





    public static int DB_VERSION = 1;

    Context context;

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY  AUTOINCREMENT, IMAGE text, DATE text, FAV text, COL4 text, COL5 text)";
        sqLiteDatabase.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }


    public long getRowCount(SQLiteDatabase mDatabase) {
        String sql = "SELECT COUNT(*) FROM " +TABLE_NAME;
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        long count = statement.simpleQueryForLong();
        return count;
    }

    public String[][] genericSelect(String select, String tableName,
                                    String where, String groupBy, String having, int noCols) {
        String strData[][] = null;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + select + " FROM " + tableName;
        if (!where.equals("")) {
            query = query + " WHERE " + where;
        }
        if (!groupBy.equals("")) {
            query = query + " GROUP BY " + groupBy;
        }
        if (!having.equals("")) {
            query = query + " HAVING " + having;
        }
        Cursor cur = null;
        try {
            cur = db.rawQuery(query, new String[] {});
            int noOfRows = cur.getCount();
            int count = 0;
            if (noOfRows > 0) {
                strData = new String[noOfRows][noCols];
                while (cur.moveToNext()) {
                    for (int i = 0; i < noCols; i++) {
                        strData[count][i] = cur.getString(i);
                    }
                    count++;
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            if (cur != null && !cur.isClosed())
                cur.close();
            // db.close();
            close();
        }
        return strData;
    }

    public boolean genericUpdate(String table, String ColNo, String updateString,
                                 String keyCol, String key) {
        boolean str_return = true;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            try {
                cv.put(ColNo, updateString);
                long rowId = db.update(table, cv,keyCol + " = ?",
                        new String[] { key });
                if (rowId == -1) {
                    str_return = false;
                }
            } catch (Exception e) {
                str_return = false;
            }
        } finally {
            // db.close();
            close();
        }
        return str_return;
    }
}