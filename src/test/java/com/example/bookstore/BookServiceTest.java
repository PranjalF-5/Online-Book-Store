package com.example.bookstore;

import com.example.bookstore.controller_and_exception.BookNotFoundException;
import com.example.bookstore.model.Book;
import com.example.bookstore.repo_and_service.BookRepository;
import com.example.bookstore.repo_and_service.BookService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void testAddBook() {
        // Arrange
        Book book = new Book();
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setPrice(BigDecimal.valueOf(9.99));
        book.setPublishedDate(LocalDate.of(2023, 1, 1));

        Mockito.when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book savedBook = bookService.addBook(book);

        // Assert
        Assertions.assertNotNull(savedBook);
        Assertions.assertEquals("Test Title", savedBook.getTitle());
        Mockito.verify(bookRepository, Mockito.times(1)).save(book);
    }

    @Test
    void testGetBookById_Found() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        Book foundBook = bookService.getBookById(1L);

        // Assert
        Assertions.assertNotNull(foundBook);
        Assertions.assertEquals(1L, foundBook.getId());
        Assertions.assertEquals("Test Title", foundBook.getTitle());
        Mockito.verify(bookRepository, Mockito.times(1)).findById(1L);
    }

    @Test
    void testGetBookById_NotFound() {
        // Arrange
        Mockito.when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(BookNotFoundException.class, () -> bookService.getBookById(2L));
        Mockito.verify(bookRepository, Mockito.times(1)).findById(2L);
    }

    @Test
    void testUpdateBook() {
        // Arrange
        Book existingBook = new Book();
        existingBook.setId(1L);
        existingBook.setTitle("Old Title");
        existingBook.setAuthor("Old Author");
        existingBook.setPrice(BigDecimal.valueOf(10.00));
        existingBook.setPublishedDate(LocalDate.of(2020, 1, 1));


        Book updatedDetails = new Book();
        updatedDetails.setTitle("New Title");
        updatedDetails.setAuthor("New Author");
        updatedDetails.setPrice(BigDecimal.valueOf(12.50));
        updatedDetails.setPublishedDate(LocalDate.of(2021, 5, 1));

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        Mockito.when(bookRepository.save(existingBook)).thenReturn(existingBook);

        // Act
        Book updatedBook = bookService.updateBook(1L, updatedDetails);

        // Assert
        Assertions.assertEquals("New Title", updatedBook.getTitle());
        Assertions.assertEquals("New Author", updatedBook.getAuthor());
        Assertions.assertEquals(BigDecimal.valueOf(12.50), updatedBook.getPrice());
        Assertions.assertEquals(LocalDate.of(2021, 5, 1), updatedBook.getPublishedDate());
    }

    @Test
    void testDeleteBook() {
        // Arrange
        Book existingBook = new Book();
        existingBook.setId(1L);

        Mockito.when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));

        // Act
        bookService.deleteBook(1L);

        // Assert
        Mockito.verify(bookRepository, Mockito.times(1)).delete(existingBook);
    }
}