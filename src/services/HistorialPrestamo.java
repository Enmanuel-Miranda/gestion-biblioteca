package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import Models.RegistrarPrestamo;


public class HistorialPrestamo {
	private List<RegistrarPrestamo> historial;
	//private PrestamoRepository repositorioPrestamo;

	public HistorialPrestamo(List<RegistrarPrestamo> historial) {
		
		this.historial = historial;
	}
public HistorialPrestamo() {
	this.historial = new ArrayList<>();
		
	}
	
	public void registroPrestamo(String isbn, String titulo, LocalDateTime fecha_hora) {
		RegistrarPrestamo prestamo = new RegistrarPrestamo(isbn, titulo, fecha_hora);
		historial.add(prestamo);
		//repositorioPrestamo.guardarPrestamo(prestamo);
		System.out.println("Prestamo registrado con exito");
	}
	
	

	public List<RegistrarPrestamo> ObtenerPrestamos(){
        return new ArrayList<>(historial);
	}
}
