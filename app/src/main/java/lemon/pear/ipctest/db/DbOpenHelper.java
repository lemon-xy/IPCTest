package lemon.pear.ipctest.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 本地数据库
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "book_data.db";

    public static final String TABLE_BOOK_NAME = "book";
    public static final String TABLE_USER_NAME = "user";

    private static final int DB_VERSION = 1;

    private String CREATE_TABLE_BOOK = "CREATE TABLE IF NOT EXISTS " +
            TABLE_BOOK_NAME + "(_ID INTEGER PRIMARY KEY," + " name TEXT)";

    private String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " +
            TABLE_USER_NAME + "(_ID INTEGER PRIMARY KEY," + " name TEXT, " + " sex INT)";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BOOK);
        db.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
