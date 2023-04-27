package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitle(String title);

    @Query("select b from Book b left join b.authors ba WHERE b.title = :title GROUP BY b "
            + "HAVING SUM(CASE WHEN ba.name IN (:authorNames) THEN 1 ELSE -1 END) = :authorNamesCount")
    List<Book> findByTitleAndAuthorNames(String title, List<String> authorNames, int authorNamesCount);

    @Query("select b from Book b join b.authors ba GROUP BY b "
            + "HAVING SUM(CASE WHEN ba.name IN (:authorNames) THEN 1 ELSE -1 END) = :authorNamesCount")
    List<Book> findByAuthorNames(List<String> authorNames, int authorNamesCount);

    Book findByIsbn(String isbn);
}
