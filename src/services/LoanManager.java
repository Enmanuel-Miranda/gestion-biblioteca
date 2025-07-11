package services;

import Models.Book;
import Models.Prestamo;
import Models.User;
import Repositories.BookRepository;
import exceptions.LibraryException;

import java.util.ArrayList;
import java.util.List;

public class LoanManager {
    private BookRepository bookRepository;
    private List<Prestamo> prestamosActivos;

    //Constructor
    public LoanManager(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
        this.prestamosActivos = new ArrayList<>();
    }

    public void borrowBook(String isbn, User usuario) throws LibraryException {
        Book book = bookRepository.findBook(isbn);

        if (!book.getDisponibilidad()) {
            throw new LibraryException("El libro con ISBN " + isbn + " ya fue prestado.");
        }

        // Registrar el préstamo
        Prestamo nuevoPrestamo = new Prestamo(usuario, book);
        prestamosActivos.add(nuevoPrestamo);

        book.setDisponibilidad(false);
    }

    public void returnBook(String isbn, User usuario) throws LibraryException {

        Book book = bookRepository.findBook(isbn);

        if (book.getDisponibilidad()) {
            throw new LibraryException("El libro con ISBN " + isbn + " ya está disponible.");
        }

        // Buscar el préstamo activo correspondiente
        Prestamo prestamoEncontrado = null;
        for (Prestamo p : prestamosActivos) {
            if (p.getLibro().getIsbn().equals(isbn)) {
                prestamoEncontrado = p;
                break;
            }
        }
        //Valida si no encontro un prestamo
        if (prestamoEncontrado == null) {
            throw new LibraryException("No se encontró ningún préstamo activo para el ISBN: " + isbn);
        }
        //Validad si el usuario que esta devolviedo es el mismo
        if (!prestamoEncontrado.getUsuario().getUsuario().equals(usuario.getUsuario())){
            throw new LibraryException("Este libro fue prestado por otro usuario. No puedes devolverlo.");
        }
        System.out.println("El libro estaba prestado por: " + prestamoEncontrado.getUsuario().getNombre());

        // Eliminar el préstamo y liberar el libro
        prestamosActivos.remove(prestamoEncontrado);
        book.setDisponibilidad(true);
    }

    public User getUsuarioQueTieneElLibro(String isbn) {
        for (Prestamo prestamo : prestamosActivos) {
            if (prestamo.getLibro().getIsbn().equals(isbn)) {
                return prestamo.getUsuario();
            }
        }
        return null; // Si no se encontró ningún préstamo para ese libro
    }

}