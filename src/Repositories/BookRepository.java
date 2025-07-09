package Repositories;

import java.util.List;
import Models.Book;
import exceptions.LibraryException;

//esta interfaz define el contrato para las operaciones de un repositorio de libros
public interface BookRepository {
	
	/*
	 * Se declara exceptiones aqui para establecer un contrato explícito de errores, obligando a los clientes a manejar fallos
	 */
    void addBook(Book book) throws LibraryException;
    void removeBook(String isbn) throws LibraryException;
    Book findBook(String isbn) throws LibraryException;
    List<Book> getAllBooks();
}