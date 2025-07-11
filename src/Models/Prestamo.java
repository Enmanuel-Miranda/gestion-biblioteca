package Models;

public class Prestamo {
    private User usuario;
    private Book libro;

    public Prestamo() {
    }

    public Prestamo(User usuario, Book libro) {
        this.usuario = usuario;
        this.libro = libro;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public Book getLibro() {
        return libro;
    }

    public void setLibro(Book libro) {
        this.libro = libro;
    }
}