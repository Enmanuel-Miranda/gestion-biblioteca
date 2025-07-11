package Project;

import Models.Book;
import Models.User;
import Repositories.*;
import services.LoanManager;
import exceptions.LibraryException;

import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        // implementacion del repositorio: son 2
        //BookRepository bookRepository = new ArrayListBookRepository(); 
        BookRepository bookRepository = new ArrayBookRepository();
        UserRepository userRepository = new ArrayListUserRepository();
        LoanManager loanManager = new LoanManager(bookRepository);
        Scanner scanner = new Scanner(System.in);

        //añadimos unos libros y usuarios
        insertarDatos(bookRepository,userRepository);


        System.out.println("Gestión de Biblioteca ***************************************");
        int option;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Mostrar todos los libros");
            System.out.println("2. Prestar un libro");
            System.out.println("3. Devolver un libro");
            System.out.println("4. Añadir un nuevo libro");
            System.out.println("5. Eliminar un libro");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                option = Integer.parseInt(scanner.nextLine());

                switch (option) {
                    case 1:
                        displayAllBooks(bookRepository);
                        break;
                    case 2:
                        System.out.print("Ingrese el ISBN del libro a prestar: ");
                        String isbnToBorrow = scanner.nextLine();
                        try {
                            loanManager.borrowBook(isbnToBorrow,new User("sdad","safsf"));
                            System.out.println("Libro con ISBN " + isbnToBorrow + " prestado exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al prestar libro: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.print("Ingrese el ISBN del libro a devolver: ");
                        String isbnToReturn = scanner.nextLine();
                        try {
                            loanManager.returnBook(isbnToReturn);
                            System.out.println("Libro con ISBN " + isbnToReturn + " devuelto exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al devolver libro: " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Ingrese ISBN: ");
                        String newIsbn = scanner.nextLine();
                        System.out.print("Ingrese Título: ");
                        String newTitulo = scanner.nextLine();
                        System.out.print("Ingrese Autor: ");
                        String newAutor = scanner.nextLine();
                        System.out.print("Ingrese Año de Publicación: ");
                        int newAnio = Integer.parseInt(scanner.nextLine());
                        try {
                            bookRepository.addBook(new Book(newIsbn, newTitulo, newAutor, newAnio));
                            System.out.println("Libro añadido exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al añadir libro: " + e.getMessage());
                        }
                        break;
                    case 5:
                        System.out.print("Ingrese el ISBN del libro a eliminar: ");
                        String isbnToRemove = scanner.nextLine();
                        try {
                            bookRepository.removeBook(isbnToRemove);
                            System.out.println("Libro con ISBN " + isbnToRemove + " eliminado exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al eliminar libro: " + e.getMessage());
                        }
                        break;
                    case 0:
                        System.out.println("Saliendo del sistema. ¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                option = -1;
            }

        } while (option != 0);

        scanner.close();
    }

    private static void displayAllBooks(BookRepository bookRepository) {
        System.out.println("\n--- Lista Actual de Libros ---");
        List<Book> allBooks = bookRepository.getAllBooks();
        if (allBooks.isEmpty()) {
            System.out.println("El repositorio está vacío.");
        } else {
            // Itera sobre cada libro y accede a sus atributos con los getters
            for (Book book : allBooks) {
                System.out.println("ISBN: " + book.getIsbn() +
                                   ", Título: " + book.getTitulo() +
                                   ", Autor: " + book.getAutor() +
                                   ", Año: " + book.getAnioPublicacion() +
                                   ", Disponibilidad: " + (book.getDisponibilidad() ? "Disponible" : "Prestado"));
            }
        }
        System.out.println("Total de libros: " + allBooks.size());
    }

    public static void insertarDatos(BookRepository bookRepository, UserRepository userRepository){
        try {
            bookRepository.addBook(new Book("978-0321356680", "Clean Code", "Robert C. Martin", 2008));
            bookRepository.addBook(new Book("978-0134685991", "Effective Java", "Joshua Bloch", 2018));
            bookRepository.addBook(new Book("978-123456789X", "The Pragmatic Programmer", "Andy Hunt", 1999));
            System.out.println("Libros iniciales cargados.");
        } catch (LibraryException e) {
            System.err.println("Error al cargar libros iniciales: " + e.getMessage());
        }
        try {
            userRepository.addUser(new User("Fernando","fgv"));
            userRepository.addUser(new User("Marce","moci"));
            System.out.println("Usuarios Agregados");
        } catch (LibraryException e) {
            System.err.println("Error al cargar Usuarios iniciales: " + e.getMessage());
        }
    }

}