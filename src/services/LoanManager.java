package services;

import Models.Book;
import Repositories.BookRepository;
import exceptions.LibraryException;

public class LoanManager {
    private BookRepository bookRepository;
    
    //Constructor
    public LoanManager(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    public void borrowBook(String isbn) throws LibraryException {
        Book book = bookRepository.findBook(isbn);// buscamos el libro

        if (!book.getDisponibilidad()) {
            throw new LibraryException("Intento de prestar un libro ya prestado: El libro con ISBN " + isbn + " no está disponible.");
        }
        book.setDisponibilidad(false);
    }

    public void returnBook(String isbn) throws LibraryException {
        Book book = bookRepository.findBook(isbn);

        if (book.getDisponibilidad()) {
            throw new LibraryException("Intento de devolver un libro no prestado: El libro con ISBN " + isbn + " ya está disponible.");
        }
        book.setDisponibilidad(true);
    }
}