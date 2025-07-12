package filters;

import Models.Book;

public class AuthorFilter implements BookFilter {
    private String autor;

    public AuthorFilter(String autor) {
        this.autor = autor;
    }

    @Override
    public boolean matches(Book book) {
        return book.getAutor().equalsIgnoreCase(autor);
    }
}
