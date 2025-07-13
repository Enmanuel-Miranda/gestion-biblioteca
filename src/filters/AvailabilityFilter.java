package filters;

import Models.Book;

public class AvailabilityFilter implements BookFilter{
    private boolean disponibilidad;

    public AvailabilityFilter(boolean disponibilidad) {
        this.disponibilidad = disponibilidad;
    }

    @Override
    public boolean matches(Book book) {
        return book.getDisponibilidad() == disponibilidad;
    }
}
