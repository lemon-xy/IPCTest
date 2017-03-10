// IBook.aidl
package lemon.pear.ipctest;

import lemon.pear.ipctest.entity.Book;

interface IManager {

    List<Book> getBookList();

    void addBook(in Book book);
}
