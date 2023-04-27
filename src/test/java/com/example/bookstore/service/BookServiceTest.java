package com.example.bookstore.service;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatusCode;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    private BookService bookService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository, authorRepository);
    }

    @Test
    void addBook_validRequest_addedSuccessfully() {
        Book bookRequest = createBookRequest();
        when(authorRepository.findByIds(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(new Author()));
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(bookRequest);
        Book book = bookService.addBook(bookRequest);
        Assertions.assertNotNull(book);
    }

    @Test
    void addBook_constraintViolationException_throwException400() {
        Book bookRequest = createBookRequest();
        when(authorRepository.findByIds(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(new Author()));
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenThrow(ConstraintViolationException.class);

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookService.addBook(bookRequest));
        assertEquals(HttpStatusCode.valueOf(400), exception.getStatusCode());
    }

    @Test
    void addBook_authorIdsNotExist_throwException400() {
        Book bookRequest = createBookRequest();
        when(authorRepository.findByIds(ArgumentMatchers.anyList())).thenReturn(Collections.emptyList());

        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookService.addBook(bookRequest));
        assertEquals(HttpStatusCode.valueOf(400), exception.getStatusCode());
        Mockito.verify(bookRepository, never()).save(Mockito.any(Book.class));
    }

    @Test
    void addBook_isbnValueNotUnique_throwException400() {
        Book bookRequest = createBookRequest();
        when(authorRepository.findByIds(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(new Author()));
        when(bookRepository.findByIsbn(ArgumentMatchers.anyString())).thenReturn(new Book());
        ResponseStatusException exception = Assertions.assertThrows(ResponseStatusException.class,
                () -> bookService.addBook(bookRequest));
        assertEquals(HttpStatusCode.valueOf(400), exception.getStatusCode());
        Mockito.verify(bookRepository, never()).save(Mockito.any(Book.class));
    }

    @Test
    void updateBook_validRequest_updatedSuccessfully() {
        Book bookRequest = createBookRequest();
        when(bookRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(new Book()));
        when(authorRepository.findByIds(ArgumentMatchers.anyList()))
                .thenReturn(Collections.singletonList(new Author()));
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(bookRequest);
        Book book = bookService.addBook(bookRequest);
        Assertions.assertNotNull(book);
    }

    private static Book createBookRequest() {
        Book book = new Book();
        book.setIsbn("ISBN-13: 978-0-596-52068-7");
        book.setTitle("title1");
        book.setGenre("genre1");
        book.setPrice(25.99);
        book.setYear(2002);
        Set<Author> authors = new HashSet<>();
        Author author1 = new Author();
        author1.setId(1);
        authors.add(author1);
        book.setAuthors(authors);
        return book;
    }
}