package Project;

import Models.Book;
import Models.User;
import Repositories.ArrayBookRepository;
import Repositories.ArrayListUserRepository;
import Repositories.BookRepository;
import Repositories.UserRepository;
import exceptions.LibraryException;
import services.LoanManager;
import java.util.List;
import java.util.Scanner;

public class prueba {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserRepository userRepository = new ArrayListUserRepository();
        BookRepository bookRepository = new ArrayBookRepository();
        LoanManager loanManager = new LoanManager(bookRepository);

        insertarDatos(bookRepository,userRepository);

        boolean validar = true;
        int opc;
        User usuarioActivo = null ;

        System.out.println("Login**********************************************");
        do{
            System.out.println("1.- Iniciar sesion");
            System.out.println("2.- Crear Sesion");
            opc = Integer.parseInt(sc.nextLine());
            switch (opc){
                case 1 -> {
                    System.out.print("Ingrese el nombre de usuario: ");
                    String name = sc.nextLine().trim();
                    try {
                        usuarioActivo = userRepository.findUser(name);
                        System.out.println("Usuario encontrado " + usuarioActivo.getNombre());
                        validar = false;
                    } catch (LibraryException e) {
                        System.err.println("Error al encontrar un usuario: "+e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Ingrese un nombre: ");
                    String nombre = sc.nextLine();
                    System.out.println("Ingrese su nick name");
                    String name = sc.nextLine();
                    try {
                        userRepository.addUser(new User(nombre,name));
                        usuarioActivo = userRepository.findUser(name);
                        System.out.println("Usuario Creado " + usuarioActivo.getNombre());
                        validar = false ;
                    } catch (LibraryException e) {
                        System.err.println("Error al registrar un usuario: "+e.getMessage());
                    }
                }
                default -> System.out.printf("Opcion no reconocida");
            }
        }while(validar);

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
                option = Integer.parseInt(sc.nextLine());

                switch (option) {
                    case 1:
                        displayAllBooks(bookRepository, loanManager);
                        break;
                    case 2:
                        System.out.print("Ingrese el ISBN del libro a prestar: ");
                        String isbnToBorrow = sc.nextLine();
                        try {
                            loanManager.borrowBook(isbnToBorrow, usuarioActivo);
                            System.out.println("Libro con ISBN " + isbnToBorrow + " prestado exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al prestar libro: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.print("Ingrese el ISBN del libro a devolver: ");
                        String isbnToReturn = sc.nextLine();
                        try {
                            loanManager.returnBook(isbnToReturn);
                            System.out.println("Libro con ISBN " + isbnToReturn + " devuelto exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al devolver libro: " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Ingrese ISBN: ");
                        String newIsbn = sc.nextLine();
                        System.out.print("Ingrese Título: ");
                        String newTitulo = sc.nextLine();
                        System.out.print("Ingrese Autor: ");
                        String newAutor = sc.nextLine();
                        System.out.print("Ingrese Año de Publicación: ");
                        int newAnio = Integer.parseInt(sc.nextLine());
                        try {
                            bookRepository.addBook(new Book(newIsbn, newTitulo, newAutor, newAnio));
                            System.out.println("Libro añadido exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al añadir libro: " + e.getMessage());
                        }
                        break;
                    case 5:
                        System.out.print("Ingrese el ISBN del libro a eliminar: ");
                        String isbnToRemove = sc.nextLine();
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

        sc.close();
    }

    private static void displayAllBooks(BookRepository bookRepository, LoanManager loanManager) {
        System.out.println("\n--- Lista Actual de Libros ---");
        List<Book> allBooks = bookRepository.getAllBooks();

        if (allBooks.isEmpty()) {
            System.out.println("El repositorio está vacío.");
        } else {
            for (Book book : allBooks) {
                System.out.print("ISBN: " + book.getIsbn() +
                        ", Título: " + book.getTitulo() +
                        ", Autor: " + book.getAutor() +
                        ", Año: " + book.getAnioPublicacion() +
                        ", Disponibilidad: " + (book.getDisponibilidad() ? "Disponible" : "Prestado"));

                // Si el libro está prestado, mostramos quién lo tiene
                if (!book.getDisponibilidad()) {
                    User u = loanManager.getUsuarioQueTieneElLibro(book.getIsbn());
                    String nombre = (u != null) ? u.getNombre() : "Desconocido";
                    System.out.print(" → Prestado por: " + nombre);
                }

                System.out.println(); // Salto de línea al final de cada libro
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
