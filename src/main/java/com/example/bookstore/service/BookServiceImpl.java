package com.example.bookstore.service;

import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.AuthorRepository;
import com.example.bookstore.repository.BookRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book addBook(Book book) {
        // if book has authors in the request, fetch authors by ids and validate if they exist
        if (CollectionUtils.isNotEmpty(book.getAuthors())) {
            List<Author> authors = fetchAndValidateAuthorsByAuthorIds(book);
            book.setAuthors(new HashSet<>(authors));
        }

        // isbn must be unique
        validateIsbn(book);

        return saveBook(book);
    }

    public Book addAuthor(Long bookId, Long authorId) {
        Book book = fetchAndValidateBookById(bookId);

        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if (authorOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id not found");
        }
        book.getAuthors().add(authorOptional.get());
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Long bookId, Book book) {
        Book bookFromDb = fetchAndValidateBookById(bookId);
        if (CollectionUtils.isNotEmpty(book.getAuthors())) {
            List<Author> authors = fetchAndValidateAuthorsByAuthorIds(book);
            book.setAuthors(new HashSet<>(authors));
        }

        validateIsbn(book, bookFromDb);

        bookFromDb.setIsbn(book.getIsbn());
        bookFromDb.setTitle(book.getTitle());
        bookFromDb.setAuthors(book.getAuthors());
        bookFromDb.setYear(book.getYear());
        bookFromDb.setPrice(book.getPrice());
        bookFromDb.setGenre(book.getGenre());
        return saveBook(bookFromDb);
    }

    @Override
    public List<Book> findBookByTitleAndOrAuthors(String title, List<String> authorNames) {
        boolean hasTitle = StringUtils.isNotBlank(title);
        boolean hasAuthors = CollectionUtils.isNotEmpty(authorNames);
        if (hasTitle) {
            if (hasAuthors) {
                return bookRepository.findByTitleAndAuthorNames(title, authorNames, authorNames.size());
            }
            return bookRepository.findByTitle(title);
        }
        if (hasAuthors) {
            return bookRepository.findByAuthorNames(authorNames, authorNames.size());
        }
        return Collections.emptyList();
    }

    @Override
    public void deleteBook(Long bookId) {
        bookRepository.deleteById(bookId);
    }

    @Override
    public Book getBookById(Long bookId) {
        return fetchAndValidateBookById(bookId);
    }

    private void validateIsbn(Book book) {
        if (StringUtils.isNotBlank(book.getIsbn()) && bookRepository.findByIsbn(book.getIsbn()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN value already exists");
        }
    }

    private void validateIsbn(Book book, Book bookFromDb) {
        if (StringUtils.isBlank(book.getIsbn())) {
            return;
        }

        if (bookFromDb.getIsbn().equalsIgnoreCase(book.getIsbn())) {
            return;
        }

        if (bookRepository.findByIsbn(book.getIsbn()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ISBN value already exists");
        }
    }

    private Book saveBook(Book book) {
        try {
            return bookRepository.save(book);
        } catch (ConstraintViolationException constraintViolationException) {
            if (constraintViolationException.getConstraintViolations() != null) {
                List<String> reasons = constraintViolationException.getConstraintViolations().stream()
                        .map(BookServiceImpl::mapConstraintViolationMessage).toList();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, reasons.toString(),
                        constraintViolationException);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, constraintViolationException.getMessage(),
                    constraintViolationException);
        }
    }

    private List<Author> fetchAndValidateAuthorsByAuthorIds(Book book) {
        List<Long> authorIds = book.getAuthors().stream().map(Author::getId).toList();
        List<Author> authors = authorRepository.findByIds(authorIds);
        if (authors.size() != authorIds.size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author id(s) is not valid");
        }
        return authors;
    }

    private Book fetchAndValidateBookById(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Book id %d not found", bookId));
        }
        return bookOptional.get();
    }

    private static String mapConstraintViolationMessage(ConstraintViolation<?> constraintViolation) {
        return String.format("%s-%s", constraintViolation.getPropertyPath(), constraintViolation.getMessage());
    }
}
