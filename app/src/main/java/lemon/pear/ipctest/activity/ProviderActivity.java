package lemon.pear.ipctest.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import lemon.pear.ipctest.Config;
import lemon.pear.ipctest.R;
import lemon.pear.ipctest.entity.Book;
import lemon.pear.ipctest.provider.BookProvider;

/**
 * ContentProvider的使用
 **/
public class ProviderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_provider);
        Uri uri = Uri.parse("content://lemon.pear.user.provider");
        getContentResolver().query(uri, null,null,null,null);
        testInsert();
        testRetrieve();
    }

    private void testInsert() {
        ContentValues values = new ContentValues();
        values.put("_id", 0);
        values.put("name", "语文");
        getContentResolver().insert(BookProvider.URI_BOOK_CONTENT_URI, values);
        values = new ContentValues();
        values.put("_id", 1);
        values.put("name", "数学");
        getContentResolver().insert(BookProvider.URI_BOOK_CONTENT_URI, values);
    }

    private void testRetrieve() {
        Cursor cursor = getContentResolver().query(BookProvider.URI_BOOK_CONTENT_URI, new String[]{"_id", "name"}, null, null, null);
        while (cursor.moveToNext()) {
            Book book = new Book();
            book.bookId = cursor.getInt(0);
            book.bookName = cursor.getString(1);
            Log.i(Config.LOG_TAG, "book:" + book.bookId + book.bookName);
        }
        cursor.close();
    }
}
