package Project;

import Models.Book;
import Models.User;
import Repositories.*;
import filters.AuthorFilter;
import filters.AvailabilityFilter;
import filters.BookFilter;
import filters.YearFilter;
import Models.RegistrarPrestamo;
import Repositories.ArrayBookRepository;
import Repositories.BookRepository;
import services.HistorialPrestamo;
import services.LoanManager;
import exceptions.LibraryException;

import java.util.List;
import java.util.Scanner;

public class App {

    private static Scanner scanner = new Scanner(System.in);
    private static HistorialPrestamo historialManager = new HistorialPrestamo();

    public static void main(String[] args) {
        // implementacion del repositorio: son 2
        //BookRepository bookRepository = new ArrayListBookRepository(); 
        BookRepository bookRepository = new ArrayBookRepository();

         // ¡Aquí está el cambio! Ya no se pasa un repositorio
        LoanManager loanManager = new LoanManager(bookRepository, historialManager);
        UserRepository userRepository = new ArrayListUserRepository();
        
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
            opc = readInt("Seleccione una opción: ");
            switch (opc) {
                case 1 -> {
                    usuarioIndentificado = iniciarSesion(scanner, userRepository);
                    if (usuarioIndentificado != null) {
                        sistemaBiblioteca(scanner, bookRepository, loanManager, usuarioIndentificado);
                    }
                }
                case 2 -> registrase(scanner, userRepository);
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
        scanner.close();
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
    public static User iniciarSesion (Scanner scanner, UserRepository userRepository){

        User usuarioActivo;
        String name = readString("Ingrese el nombre de usuario: ");
        try {
            usuarioActivo = userRepository.findUser(name);
            System.out.println("Usuario encontrado " + usuarioActivo.getNombre());
            return usuarioActivo;
        } catch (LibraryException e) {
            System.err.println("Error al encontrar un usuario: "+e.getMessage());
        }
        return null; // Si no cumple
    }
    public static void registrase(Scanner scanner, UserRepository userRepository){


        String nombre = readString("Ingrese su nombre: ");
        String name = readString("Ingrese su nickname: ");
        try {
            userRepository.addUser(new User(nombre,name));
            System.out.println("Usuario creado correctamente ");
        } catch (LibraryException e) {
            System.err.println("Error al registrar un usuario: "+e.getMessage());
        }
    }
    public static void sistemaBiblioteca(Scanner scanner,BookRepository bookRepository, LoanManager loanManager, User usuarioIndentificado) {

        System.out.println("Gestión de Biblioteca ***************************************");
        int option;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Mostrar todos los libros");
            System.out.println("2. Prestar un libro");
            System.out.println("3. Devolver un libro");
            System.out.println("4. Añadir un nuevo libro");
            System.out.println("5. Eliminar un libro");
            System.out.println("6. Ver historial de prestamos"); //se añadio esto de aqui
            System.out.println("7. Búsqueda personalizada");
            System.out.println("0. Cerrar Sesion");
            System.out.print("Seleccione una opción: ");

            try {
                option = readInt("Ingrese su opción: ");

                switch (option) {
                    case 1:
                        displayAllBooks(bookRepository, loanManager);
                        break;
                    case 2:
                        String isbnToBorrow = readString("Ingrese el ISBN del libro a prestar: ");
                        try {
                            loanManager.borrowBook(isbnToBorrow, usuarioIndentificado);
                            System.out.println("Libro con ISBN " + isbnToBorrow + " prestado exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al prestar libro: " + e.getMessage());
                        }
                        break;
                    case 3:
                        String isbnToReturn = readString("Ingrese el ISBN del libro a devolver: ");
                        try {
                            loanManager.returnBook(isbnToReturn,usuarioIndentificado);
                            System.out.println("Libro con ISBN " + isbnToReturn + " devuelto exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al devolver libro: " + e.getMessage());
                        }
                        break;
                    case 4:
                        String newIsbn = readString("Ingrese ISBN: ");
                        String newTitulo = readString("Ingrese Título: ");
                        String newAutor = readString("Ingrese Autor: ");
                        int newAnio = readInt("Ingrese Año de Publicación: ");
                        try {
                            bookRepository.addBook(new Book(newIsbn, newTitulo, newAutor, newAnio));
                            System.out.println("Libro añadido exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al añadir libro: " + e.getMessage());
                        }
                        break;
                    case 5:
                        String isbnToRemove = readString("ISBN del libro a eliminar: ");
                        try {
                            bookRepository.removeBook(isbnToRemove);
                            System.out.println("Libro con ISBN " + isbnToRemove + " eliminado exitosamente.");
                        } catch (LibraryException e) {
                            System.err.println("Error al eliminar libro: " + e.getMessage());
                        }
                        break;
                    case 6: //ver el historial
                        System.out.print("Historial de prstamos");
                        List<RegistrarPrestamo> prestamoRegistrado = historialManager.ObtenerPrestamos();
                        
                        if(prestamoRegistrado.isEmpty()){
                        System.out.println("No hay prestamos");
                        }else {
                        for (RegistrarPrestamo rp : prestamoRegistrado ){
                        	System.out.println(rp);
                        }
                        }
                        break;
                    case 7:
                        searchWithFilter(bookRepository);
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

    private static void displayAllBooks(BookRepository bookRepository) {
        System.out.println("\n--- Lista Actual de Libros ---");
        List<Book> allBooks = bookRepository.getAllBooks();
        if (allBooks.isEmpty()) {
            System.out.println("El repositorio está vacío.");
        } else {
            // Itera sobre cada libro y accede a sus atributos con los getters
            for (Book book : allBooks) {
                System.out.println("ISBN: " + book.getIsbn() +
                        " | Título: " + book.getTitulo() +
                        " | Autor: " + book.getAutor() +
                        " | Año: " + book.getAnioPublicacion() +
                        " | Disponible: " + (book.getDisponibilidad() ? "Sí" : "No"));
            }
        }
        System.out.println("Total de libros: " + allBooks.size());
    }
    private static void searchWithFilter(BookRepository selectedRepo) {
        System.out.println("=== Búsqueda personalizada ===");
        System.out.println("1. Buscar por autor");
        System.out.println("2. Buscar por año de publicación");
        System.out.println("3. Buscar por disponibilidad");

        int choice = readInt("Seleccione el tipo de búsqueda:");
        BookFilter filter = null;

        switch (choice) {
            case 1 -> {
                String author = readString("Ingrese el nombre o parte del nombre del autor:");
                filter = new AuthorFilter(author);
            }
            case 2 -> {
                int year = readInt("Ingrese el año de publicación:");
                filter = new YearFilter(year);
            }
            case 3 -> {
                System.out.println("¿Qué desea buscar?");
                System.out.println("1. Libros disponibles");
                System.out.println("2. Libros prestados");
                int dispChoice = readInt("Opción:");
                filter = new AvailabilityFilter(dispChoice == 1);
            }
            default -> {
                System.out.println("Opción inválida.");
                return;
            }
        }

        System.out.println("=== Resultados ===");
        boolean found = false;
        for (Book book : selectedRepo.getAllBooks()) {
            if (filter.matches(book)) {
                System.out.println("ISBN: " + book.getIsbn() +
                        " | Título: " + book.getTitulo() +
                        " | Autor: " + book.getAutor() +
                        " | Año: " + book.getAnioPublicacion() +
                        " | Disponible: " + (book.getDisponibilidad() ? "Sí" : "No"));
                found = true;
            }
        }

        if (!found) {
            System.out.println("No se encontraron libros que coincidan con el criterio.");
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt + " ");
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        System.out.print(prompt + " ");
        while (!scanner.hasNextInt()) {
            System.out.println("Debe ingresar un número.");
            System.out.print(prompt + " ");
            scanner.next();
        }
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }
}