package Repositories;

import Models.Book;
import exceptions.LibraryException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// usando un arreglo estático.
public class ArrayBookRepository implements BookRepository {
    private Book[] books;	//Arreglo
    private int count; // Libros actualmente en el arreglo
    private static final int DEFAULT_CAPACITY = 10; // Capacidad inicial

    //Constructor
    public ArrayBookRepository() {
        this.books = new Book[DEFAULT_CAPACITY];	// Inicializa el arreglo con el tamaño fijo.
        this.count = 0;
    }

    @Override
    public void addBook(Book book) throws LibraryException {
        // Valida ISBN único.
        for (int i = 0; i < count; i++) {
            if (books[i].getIsbn().equals(book.getIsbn())) {
                throw new LibraryException("ISBN duplicado: " + book.getIsbn() + " ya existe.");
            }
        }

        // Verifica si el arreglo está lleno.
        if (count >= books.length) {
            throw new LibraryException("El repositorio está lleno (capacidad máxima: " + books.length + ").");
        }

        books[count] = book; // Añade el libro
        count++; // Incrementa el contador
    }

    @Override
    public void removeBook(String isbn) throws LibraryException {
        int indexToRemove = -1;
        // Busca el índice del libro.
        for (int i = 0; i < count; i++) {
            if (books[i].getIsbn().equals(isbn)) {
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove == -1) {
            throw new LibraryException("Libro no encontrado para eliminar con ISBN: " + isbn);
        }

        // Mueve elementos para llenar el espacio vacío.
        for (int i = indexToRemove; i < count - 1; i++) {
            books[i] = books[i + 1];
        }
        books[count - 1] = null; // Limpia la última posición.
        count--; // Decrementa el contador.
    }

    @Override
    public Book findBook(String isbn) throws LibraryException {
        // Busca y retorna el libro por ISBN.
        for (int i = 0; i < count; i++) {
            if (books[i].getIsbn().equals(isbn)) {
                return books[i];
            }
        }
        throw new LibraryException("Libro no encontrado con ISBN: " + isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        // Retorna una copia de la lista de libros existentes.
        List<Book> allBooks = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            allBooks.add(books[i]);
        }
        return allBooks;
    }

    // Método para buscar libros por autor usando Streams.
    public List<Book> getBooksByAuthor(String author) {
        return Arrays.stream(books, 0, count)
                     .filter(book -> book.getAutor().equalsIgnoreCase(author))
                     .collect(Collectors.toList());
    }
}