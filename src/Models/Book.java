package Models;

import exceptions.LibraryException;

public class Book {
	//atributos
	private String isbn;
	private String titulo;
	private String autor;
	private int anioPublicacion;
	private boolean disponibilidad;
	
	//constructor  
    public Book(String isbn, String titulo, String autor, int anioPublicacion) throws LibraryException {
    	//Validaciones de formato
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new LibraryException("El ISBN no puede ser nulo o vacío.");
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new LibraryException("El título no puede ser nulo o vacío.");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new LibraryException("El autor no puede ser nulo o vacío.");
        }
        if (anioPublicacion <= 0) { //no se permiten numeros negativos
            throw new LibraryException("El año de publicación debe ser un número positivo.");
        }

        this.isbn = isbn.trim();
        this.titulo = titulo.trim();
        this.autor = autor.trim();
        this.anioPublicacion = anioPublicacion;
        this.disponibilidad = true; //al crear un libro, siempre estara disponible
    }

    // Getters de los atributos
    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public boolean getDisponibilidad() {
        return disponibilidad;
    }

    // Setters con validaciones
    	// El ISBN no tiene un setter porque es único e inmutable
    public void setTitulo(String titulo) throws LibraryException { 
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new LibraryException("El título no puede ser nulo o vacío.");
        }
        this.titulo = titulo.trim();
    }

    public void setAutor(String autor) throws LibraryException { 
        if (autor == null || autor.trim().isEmpty()) {
            throw new LibraryException("El autor no puede ser nulo o vacío.");
        }
        this.autor = autor.trim();
    }

    public void setAnioPublicacion(int anioPublicacion) throws LibraryException {
        if (anioPublicacion <= 0) {
            throw new LibraryException("El año de publicación debe ser un número positivo.");
        }
        this.anioPublicacion = anioPublicacion;
    }

    public void setDisponibilidad(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }
}