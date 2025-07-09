package exceptions;
public class LibraryException extends Exception{
	
	public LibraryException(String message) {
        // Constructor con un mensaje, ideal para errores directos.
        super(message);
    }

    public LibraryException(String message, Throwable cause) {
        // Constructor con mensaje y causa, útil para encadenar excepciones.
        super(message, cause);
    }

}
