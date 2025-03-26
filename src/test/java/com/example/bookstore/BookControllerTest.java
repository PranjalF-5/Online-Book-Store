package com.example.bookstore;

import com.example.bookstore.controller_and_exception.BookController;
import com.example.bookstore.model.Book;
import com.example.bookstore.repo_and_service.BookService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void testGetAllBooks() throws Exception {
        // Arrange
        Book book1 = new Book("Title1", "Author1", BigDecimal.valueOf(9.99), LocalDate.of(2023, 1, 1));
        book1.setId(1L);

        Book book2 = new Book("Title2", "Author2", BigDecimal.valueOf(19.99), LocalDate.of(2023, 2, 1));
        book2.setId(2L);

        List<Book> books = Arrays.asList(book1, book2);
        Mockito.when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Title1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].author").value("Author2"));
    }

    @Test
    void testAddBook() throws Exception {
        // Arrange
        Book book = new Book("New Book", "New Author", BigDecimal.valueOf(10.00), LocalDate.of(2023, 5, 5));
        book.setId(1L);

        Mockito.when(bookService.addBook(Mockito.any(Book.class))).thenReturn(book);

        // Act & Assert
        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"New Book\", " +
                                "\"author\":\"New Author\", " +
                                "\"price\":10.00, " +
                                "\"publishedDate\":\"2023-05-05\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("New Book"));
    }

    @Test
    void testGetBookById() throws Exception {
        // Arrange
        Book book = new Book("Book Title", "Book Author", BigDecimal.valueOf(12.50), LocalDate.of(2022, 6, 1));
        book.setId(1L);

        Mockito.when(bookService.getBookById(1L)).thenReturn(book);

        // Act & Assert
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Book Title"))
                .andExpect(jsonPath("$.price").value(12.50));
    }

    @Test
    void testUpdateBook() throws Exception {
        // Arrange
        Book updatedBook = new Book("Updated Title", "Updated Author", BigDecimal.valueOf(20.00), LocalDate.of(2023, 3, 1));
        updatedBook.setId(1L);

        Mockito.when(bookService.updateBook(Mockito.eq(1L), Mockito.any(Book.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated Title\", " +
                                "\"author\":\"Updated Author\", " +
                                "\"price\":20.00, " +
                                "\"publishedDate\":\"2023-03-01\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.price").value(20.00));
    }

    @Test
    void testDeleteBook() throws Exception {
        // No return value, just verify the status
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }
}