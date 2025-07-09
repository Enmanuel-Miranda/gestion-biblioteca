package services;

import java.time.LocalDateTime;

import Models.Book;
import Repositories.BookRepository;
import exceptions.LibraryException;

public class LoanManager {
    private BookRepository bookRepository;
    private HistorialPrestamo historialPrestamo;
    
    //Constructor
    public LoanManager(BookRepository bookRepository, HistorialPrestamo historialPrestamo) {
		super();
		this.bookRepository = bookRepository;
		this.historialPrestamo = historialPrestamo;
	}


    public void borrowBook(String isbn) throws LibraryException {
        Book book = bookRepository.findBook(isbn);// buscamos el libro

        if (!book.getDisponibilidad()) {
            throw new LibraryException("Intento de prestar un libro ya prestado: El libro con ISBN " + isbn + " no está disponible.");
        }
        book.setDisponibilidad(false);
        
        String isbnBook = book.getIsbn();
        String tituloBook = book.getTitulo();
        LocalDateTime fecha_horaBook = LocalDateTime.now();
        
        historialPrestamo.registroPrestamo(isbnBook, tituloBook, fecha_horaBook);
        
    }

    

	public void returnBook(String isbn) throws LibraryException {
        Book book = bookRepository.findBook(isbn);

        if (book.getDisponibilidad()) {
            throw new LibraryException("Intento de devolver un libro no prestado: El libro con ISBN " + isbn + " ya está disponible.");
        }
        book.setDisponibilidad(true);
    }
}