package ru.dvfu.mrcpk.popovich.androidapp051db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "personDB";
    public static final String TABLE_MAIN = "personTable";

    public static final String TABLE_MAIN_ID = "_id";
    public static final String TABLE_MAIN_FIRSTNAME = "firstname";
    public static final String TABLE_MAIN_LASTNAME = "lastname";
    public static final String TABLE_MAIN_EMAIL = "eMail";
    public static final String TABLE_MAIN_PHONENUM = "phoneNum";

    private static final String DATABASE_CREATE =
            "create table " + TABLE_MAIN + "(" +
                    TABLE_MAIN_ID + " integer primary key autoincrement, " +
                    TABLE_MAIN_FIRSTNAME + " text, " +
                    TABLE_MAIN_LASTNAME + " text, " +
                    TABLE_MAIN_PHONENUM + " text, " +
                    TABLE_MAIN_EMAIL + " text " +
                    ");";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(TABLE_MAIN, null, null, null, null, null, null);
    }

    // Выбор конкретного элемента
    public Cursor getElement(long id){
        return mDB.query(TABLE_MAIN, null, TABLE_MAIN_ID + " = ?", new String[]{ String.valueOf(id)}, null, null, null);
    }

    // Обновление записи
    public int updateRec(ContentValues contentValues, long id){
        return mDB.update(TABLE_MAIN, contentValues, TABLE_MAIN_ID + " = ?", new String[]{ String.valueOf(id) });
    }

    // добавить запись в DB_TABLE
    public void addRec(ContentValues contentValues) {
        mDB.insert(TABLE_MAIN, null, contentValues);
    }

    // удалить запись из DB_TABLE
    public void delRec(long id) {
        mDB.delete(TABLE_MAIN, TABLE_MAIN_ID + " = " + id, null);
    }

    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

            ContentValues contentValues = new ContentValues();
            for (int i = 1; i < 2; i++) {
                contentValues.put(TABLE_MAIN_FIRSTNAME, "Ivan");
                contentValues.put(TABLE_MAIN_LASTNAME, "Ivanov");
                db.insert(TABLE_MAIN, null, contentValues);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table if exists " + TABLE_MAIN);
            onCreate(db);
        }
    }
}
