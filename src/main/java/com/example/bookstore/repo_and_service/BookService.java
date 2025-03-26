package com.example.bookstore.repo_and_service;

import com.example.bookstore.controller_and_exception.BookNotFoundException;
import com.example.bookstore.model.Book;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        logger.info("Adding new book: {}", book.getTitle());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        logger.info("Retrieving all books...");
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        logger.info("Retrieving book with ID: {}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", id);
                    return new BookNotFoundException("Book not found with ID: " + id);
                });
    }

    public Book updateBook(Long id, Book bookDetails) {
        logger.info("Updating book with ID: {}", id);
        Book book = getBookById(id);
        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setPrice(bookDetails.getPrice());
        book.setPublishedDate(bookDetails.getPublishedDate());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        logger.info("Deleting book with ID: {}", id);
        Book book = getBookById(id);
        bookRepository.delete(book);
    }
}