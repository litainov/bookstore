package com.example.bookstore.controller;

import com.example.bookstore.entity.Book;
import com.example.bookstore.service.BookServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/v1/bookstore/books")
public class BookController {

    private final BookServiceImpl bookService;

    public BookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    @PutMapping("/{bookId}/authors/{authorId}")
    @ResponseStatus(HttpStatus.OK)
    public Book addBook(@PathVariable Long bookId, @PathVariable Long authorId) {
        return bookService.addAuthor(bookId, authorId);
    }

    @PutMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@PathVariable Long bookId, @RequestBody Book book) {
        return bookService.updateBook(bookId, book);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getBooksByTitleAndOrAuthorId(@RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "authorName", required = false) List<String> authorNames) {
        if (StringUtils.isBlank(title) && CollectionUtils.isEmpty(authorNames)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either title or authorName must be provided");
        }
        return bookService.findBookByTitleAndOrAuthors(title, authorNames);
    }

    @GetMapping("/{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public Book getBookById(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }

    @DeleteMapping("/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
    }

}
