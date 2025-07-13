package filters;

import Models.Book;

public interface BookFilter {
    boolean matches(Book book);
}
