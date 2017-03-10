package lemon.pear.ipctest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import lemon.pear.ipctest.IManager;
import lemon.pear.ipctest.entity.Book;

/**
 * AIDL测试服务
 */
public class ManagerService extends Service {

    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();

    private Binder mBinder = new IManager.Stub(){

        @Override
        public List<Book> getBookList() throws RemoteException {
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            bookList.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        bookList.add(new Book(1, "语文"));
        bookList.add(new Book(2, "数学"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
