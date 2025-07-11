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
        //Instanciar
        Scanner sc = new Scanner(System.in);
        UserRepository userRepository = new ArrayListUserRepository();
        BookRepository bookRepository = new ArrayBookRepository();
        LoanManager loanManager = new LoanManager(bookRepository);
        //Carga de Datos
        insertarDatos(bookRepository, userRepository);
        //Variables
        User usuarioIndentificado;
        boolean bandera = true;
        int opc;
        //Sistema
        while (bandera) {
            System.out.println("Login**********************************************");
            System.out.println("1.- Iniciar sesion");
            System.out.println("2.- Crear Sesion");
            System.out.println("3.- Cerrar Programa");
            opc = Integer.parseInt(sc.nextLine());
            switch (opc) {
                case 1 -> {
                    usuarioIndentificado = iniciarSesion(sc, userRepository);
                    if (usuarioIndentificado != null) {
                        sistemaBiblioteca(sc, bookRepository, loanManager, usuarioIndentificado);
                    }
                }
                case 2 -> registrase(sc, userRepository);
                case 3 -> {
                    bandera = false;
                    System.out.println("Programa cerrado.");
                }
                default -> {
                    System.out.println("Opción no válida.");
                    System.out.println("-------------------------------------");
                }
            }
        }
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
    public static User iniciarSesion (Scanner sc, UserRepository userRepository){

        User usuarioActivo;
        System.out.print("Ingrese el nombre de usuario: ");
        String name = sc.nextLine().trim();
        try {
            usuarioActivo = userRepository.findUser(name);
            System.out.println("Usuario encontrado " + usuarioActivo.getNombre());
            return usuarioActivo;
        } catch (LibraryException e) {
            System.err.println("Error al encontrar un usuario: "+e.getMessage());
        }
        return null; // Si no cumple
    }
    public static void registrase(Scanner sc, UserRepository userRepository){
        System.out.print("Ingrese un nombre: ");
        String nombre = sc.nextLine();
        System.out.println("Ingrese su nick name");
        String name = sc.nextLine();
        try {
            userRepository.addUser(new User(nombre,name));
            System.out.println("Usuario creado correctamente ");
        } catch (LibraryException e) {
            System.err.println("Error al registrar un usuario: "+e.getMessage());
        }
    }
    public static void sistemaBiblioteca(Scanner sc,BookRepository bookRepository, LoanManager loanManager, User usuarioIndentificado) {

        System.out.println("Gestión de Biblioteca ***************************************");
        int option;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Mostrar todos los libros");
            System.out.println("2. Prestar un libro");
            System.out.println("3. Devolver un libro");
            System.out.println("4. Añadir un nuevo libro");
            System.out.println("5. Eliminar un libro");
            System.out.println("0. Cerrar Sesion");
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
                            loanManager.borrowBook(isbnToBorrow, usuarioIndentificado);
                            System.out.println("Libro con ISBN " + isbnToBorrow + " prestado exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al prestar libro: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.print("Ingrese el ISBN del libro a devolver: ");
                        String isbnToReturn = sc.nextLine();
                        try {
                            loanManager.returnBook(isbnToReturn,usuarioIndentificado);
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
                        usuarioIndentificado = null;
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                option = -1;
            }

        } while (option != 0);

    }

}