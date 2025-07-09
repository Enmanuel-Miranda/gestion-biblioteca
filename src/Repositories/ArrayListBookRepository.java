package Repositories;

import Models.Book;
import exceptions.LibraryException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListBookRepository implements BookRepository {
    private List<Book> books;

    //Constructor
    public ArrayListBookRepository() {
        this.books = new ArrayList<>();
    }

    @Override
    public void addBook(Book book) throws LibraryException {
        // Valida ISBN único iterando la lista.
        for (Book existingBook : books) {
            if (existingBook.getIsbn().equals(book.getIsbn())) {
                throw new LibraryException("ISBN duplicado: " + book.getIsbn() + " ya existe.");
            }
        }
        books.add(book);
    }

    @Override
    public void removeBook(String isbn) throws LibraryException {
        // Busca el libro y lo elimina. Lanza excepción si no lo encuentra.
        Book bookToRemove = findBook(isbn);
        books.remove(bookToRemove);
    }

    @Override
    public Book findBook(String isbn) throws LibraryException {
        // Busca y retorna el libro por ISBN. Lanza excepción si no lo encuentra.
        for (Book book : books) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        throw new LibraryException("Libro no encontrado con ISBN: " + isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        // Retorna una copia de la lista de libros.
        return new ArrayList<>(books);
    }
}