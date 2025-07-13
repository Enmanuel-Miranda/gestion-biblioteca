package Models;

import java.time.LocalDateTime;

public class RegistrarPrestamo {
	private String isbn;
	private String titulo;
	private LocalDateTime fecha_hora;
	
	public RegistrarPrestamo(String isbn, String titulo, LocalDateTime fecha_hora) {
		super();
		this.isbn = isbn;
		this.titulo = titulo;
		this.fecha_hora = fecha_hora;
	}

	

	public RegistrarPrestamo() {
		
	}



	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public LocalDateTime getFecha_hora() {
		return fecha_hora;
	}

	public void setFecha_hora(LocalDateTime fecha_hora) {
		this.fecha_hora = fecha_hora;
	}
	
	@Override
	public String toString() {
		return "RegistroPrestamo{" +
	               "ISBN='" + isbn + '\'' +
	               ", Titulo='" + titulo + '\'' +
	               ", Fecha/Hora Prestamo=" + fecha_hora +
	               '}';
	}
	
	

}
