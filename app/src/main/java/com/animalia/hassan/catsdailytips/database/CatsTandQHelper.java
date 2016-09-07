package com.animalia.hassan.catsdailytips.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.animalia.hassan.catsdailytips.fragmentsTips.Tips;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Hassan on 6/6/2016.
 */

public class CatsTandQHelper extends SQLiteOpenHelper {


    private Context appContext;
    public static final String DB_NAME = "Cats_Daily_Tips";
    public static final int DB_version = 1;
    public static final String CREATE_QUERY_CATS = "CREATE TABLE " + CatTips.TipsEntry.TABLE_NAME +
            "(" + CatTips.TipsEntry.Q_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CatTips.TipsEntry.Q_COLUMN_QUESTION + " TEXT," +
            CatTips.TipsEntry.Q_COLUMN_ANSWER + " TEXT," + CatTips.TipsEntry.Q_COLUMN_FAVOURITE +
            " TEXT DEFAULT false," + CatTips.TipsEntry.Q_COLUMN_NOTE_ID + " INTEGER UNIQUE," + CatTips.TipsEntry.Q_COLUMN_COMMENT +
            " TEXT)";
    public static final String CREATE_QUERY_CATSQ = "CREATE TABLE " + CatQues.QuesEntry.TABLE_NAME +
            "(" + CatQues.QuesEntry.Q_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CatQues.QuesEntry.Q_COLUMN_QUESTION + " TEXT," +
            CatQues.QuesEntry.Q_COLUMN_ANSWER + " TEXT," + CatQues.QuesEntry.Q_COLUMN_FAVOURITE +
            " TEXT DEFAULT false," + CatQues.QuesEntry.Q_COLUMN_NOTE_ID + " INTEGER UNIQUE," + CatQues.QuesEntry.Q_COLUMN_COMMENT +
            " TEXT)";
    public static final String DROP_QUERY_CATS = "drop table if exist" + CatTips.TipsEntry.TABLE_NAME + ";";
    public static final String DROP_QUERY_CATSQ = "drop table if exist" + CatQues.QuesEntry.TABLE_NAME + ";";


    private static CatsTandQHelper sInstance;

    // ...

    public static synchronized CatsTandQHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        Log.d("database called . . .", "old");

        if (sInstance == null) {
            sInstance = new CatsTandQHelper(context.getApplicationContext());
            Log.d("database called . . .", "new");
        }
        return sInstance;

    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */


    public CatsTandQHelper(Context context) {

        super(context, DB_NAME, null, DB_version);
        this.appContext = context;

        Log.d("Database operations", "Database Created...");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_CATS);
        db.execSQL(CREATE_QUERY_CATSQ);
        Log.d("Database Operations", "Table Created....");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_QUERY_CATS);
        db.execSQL(CREATE_QUERY_CATSQ);
        Log.d("Database Operations", "Database updated....");

    }

    public void putInformationCats(int _id, String question, String answer, String favourite, int q_note_id, String comment, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CatTips.TipsEntry.Q_ID, _id);
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_QUESTION, question);
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_ANSWER, answer);
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_FAVOURITE, favourite);
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_NOTE_ID, q_note_id);
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_COMMENT, comment);
        long l = db.insertWithOnConflict(CatTips.TipsEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("Database Operations", "One Row Inserted....");
    }
    public void putInformationCatsQ(int _id, String question, String answer, String favourite, int q_note_id, String comment, SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CatQues.QuesEntry.Q_ID, _id);
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_QUESTION, question);
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_ANSWER, answer);
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_FAVOURITE, favourite);
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_NOTE_ID, q_note_id);
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_COMMENT, comment);
        long l = db.insertWithOnConflict(CatQues.QuesEntry.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d("Database Operations", "One Row Inserted....");
    }

    public Cursor getTipsInfo(SQLiteDatabase db) {

        SharedPreferences pref = appContext.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int rowsAdd = pref.getInt("rowsAdd", 20);             // getting Integer;

        String[] projection = {CatTips.TipsEntry.Q_ID, CatTips.TipsEntry.Q_COLUMN_QUESTION,
                CatTips.TipsEntry.Q_COLUMN_ANSWER, CatTips.TipsEntry.Q_COLUMN_FAVOURITE,
                CatTips.TipsEntry.Q_COLUMN_NOTE_ID, CatTips.TipsEntry.Q_COLUMN_COMMENT};
        Cursor cursor = db.query(CatTips.TipsEntry.TABLE_NAME, projection, null, null, null, null, CatTips.TipsEntry.Q_ID + "  LIMIT 0," + rowsAdd);
        return cursor;
    }
    public Cursor getQuesInfo(SQLiteDatabase db) {

        SharedPreferences pref = appContext.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int rowsAdd = pref.getInt("rowsAdd", 10);             // getting Integer;

        String[] projection = {CatTips.TipsEntry.Q_ID, CatTips.TipsEntry.Q_COLUMN_QUESTION,
                CatQues.QuesEntry.Q_COLUMN_ANSWER, CatQues.QuesEntry.Q_COLUMN_FAVOURITE,
                CatQues.QuesEntry.Q_COLUMN_NOTE_ID, CatQues.QuesEntry.Q_COLUMN_COMMENT};
        Cursor cursor = db.query(CatQues.QuesEntry.TABLE_NAME, projection, null, null, null, null, CatQues.QuesEntry.Q_ID + "  LIMIT 0," + rowsAdd);
        return cursor;
    }
    public Cursor getFavTipsInfo(SQLiteDatabase db) {

        SharedPreferences pref = appContext.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int rowsAdd = pref.getInt("rowsAdd", 15);             // getting Integer;
        final String where = CatTips.TipsEntry.Q_COLUMN_FAVOURITE + " = ?";
        final String [] whereArgs = {"true"};

        Cursor cursor = db.query(CatTips.TipsEntry.TABLE_NAME, null, where, whereArgs, null, null,  CatTips.TipsEntry.Q_ID + "  LIMIT 0," + rowsAdd);


        return cursor;
    }
    public Cursor getFavQuesInfo(SQLiteDatabase db) {

        SharedPreferences pref = appContext.getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        int rowsAdd = pref.getInt("rowsAdd", 5);             // getting Integer;

        final String where = CatQues.QuesEntry.Q_COLUMN_FAVOURITE + " = ?";
        final String [] whereArgs = {"true"};

        Cursor cursor = db.query(CatQues.QuesEntry.TABLE_NAME, null, where, whereArgs, null, null, CatQues.QuesEntry.Q_ID + "  LIMIT 0," + rowsAdd);


        return cursor;
    }

    public Tips getOneTip(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CatTips.TipsEntry.TABLE_NAME, new String[]{CatTips.TipsEntry.Q_ID, CatTips.TipsEntry.Q_COLUMN_QUESTION,
                        CatTips.TipsEntry.Q_COLUMN_ANSWER, CatTips.TipsEntry.Q_COLUMN_FAVOURITE,
                        CatTips.TipsEntry.Q_COLUMN_NOTE_ID, CatTips.TipsEntry.Q_COLUMN_COMMENT}, CatTips.TipsEntry.Q_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Tips contact = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {

                    contact = new Tips(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), Integer.parseInt(cursor.getString(4)), cursor.getString(5));
                    // return contact

                } else {
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return contact;
    }
    public Tips getOneQues(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(CatQues.QuesEntry.TABLE_NAME, new String[]{CatQues.QuesEntry.Q_ID, CatQues.QuesEntry.Q_COLUMN_QUESTION,
                        CatQues.QuesEntry.Q_COLUMN_ANSWER, CatQues.QuesEntry.Q_COLUMN_FAVOURITE,
                        CatQues.QuesEntry.Q_COLUMN_NOTE_ID, CatQues.QuesEntry.Q_COLUMN_COMMENT}, CatQues.QuesEntry.Q_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        Tips contact = null;
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {

                    contact = new Tips(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1), cursor.getString(2),
                            cursor.getString(3), Integer.parseInt(cursor.getString(4)), cursor.getString(5));
                    // return contact

                } else {
                }
            } catch (CursorIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return contact;
    }


    public void update_Tip_byID(int _id, String question, String answer, String favourite, String comment  ){

        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = { String.valueOf(_id) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_FAVOURITE,favourite);
        contentValues.put(CatTips.TipsEntry.Q_COLUMN_COMMENT,comment);
        db.update(CatTips.TipsEntry.TABLE_NAME, contentValues, CatTips.TipsEntry.Q_ID+ " LIKE ?", selectionArgs);
        String resultQ = String.valueOf(db.update(CatTips.TipsEntry.TABLE_NAME, contentValues, CatTips.TipsEntry.Q_ID+ " LIKE ?", selectionArgs));
        Log.d("Database Operations",resultQ);

    }    public void update_QUE_byID(int _id, String question, String answer, String favourite, String comment  ){

        SQLiteDatabase db = this.getWritableDatabase();
        String[] selectionArgs = { String.valueOf(_id) };
        ContentValues contentValues = new ContentValues();
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_FAVOURITE,favourite);
        contentValues.put(CatQues.QuesEntry.Q_COLUMN_COMMENT,comment);
        db.update(CatQues.QuesEntry.TABLE_NAME, contentValues, CatQues.QuesEntry.Q_ID+ " LIKE ?", selectionArgs);
        String resultQ = String.valueOf(db.update(CatQues.QuesEntry.TABLE_NAME, contentValues, CatQues.QuesEntry.Q_ID+ " LIKE ?", selectionArgs));
        Log.d("Database Operations",resultQ);

    }




}
