package filters;

import Models.Book;

public class YearFilter implements BookFilter{
    private int anioPublicacion;

    public YearFilter(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    @Override
    public boolean matches(Book book) {
        return book.getAnioPublicacion() == anioPublicacion;
    }
}
