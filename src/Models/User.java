package Models;

import exceptions.LibraryException;

public class User {

    private static int contadorId ;
    private final String id ;
    private  String nombre ;


    public User (String nombre) throws LibraryException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new LibraryException("El nombre no puede ser nulo o vacío.");
        }

        this.id = String.valueOf(++User.contadorId);
        this.nombre = nombre;
    }

    public User(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws LibraryException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new LibraryException("El nombre no puede ser nulo o vacío.");
        }
        this.nombre = nombre;
    }
}
