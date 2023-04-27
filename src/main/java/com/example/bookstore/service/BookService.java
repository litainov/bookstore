package com.example.bookstore.service;

import com.example.bookstore.entity.Book;

import java.util.List;

public interface BookService {
    Book addBook(Book book);

    Book updateBook(Long bookId, Book book);

    List<Book> findBookByTitleAndOrAuthors(String title, List<String> authorNames);

    void deleteBook(Long bookId);

    Book getBookById(Long bookId);
}
