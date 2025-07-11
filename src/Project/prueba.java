package Project;

import Models.Book;
import Models.User;
import Repositories.ArrayListUserRepository;
import Repositories.UserRepository;
import exceptions.LibraryException;
import jdk.swing.interop.SwingInterOpUtils;

import java.util.Scanner;

public class prueba {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserRepository userRepository = new ArrayListUserRepository();

        boolean validar = true;
        int opc;
        User usuarioActivo ;

        System.out.println("Login**********************************************");
        do{
            System.out.println("1.- Iniciar sesion");
            System.out.println("2.- Crear Sesion");
            opc = Integer.parseInt(sc.nextLine());
            switch (opc){
                case 1 -> {
                    System.out.print("Ingrese el nombre de usuario: ");
                    String name = sc.nextLine().trim();
                    try {
                        usuarioActivo = userRepository.findUser(name);
                        System.out.println("Usuario encontrado " + usuarioActivo.getNombre());
                        validar = false;
                    } catch (LibraryException e) {
                        System.err.println("Error al encontrar un usuario: "+e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Ingrese un nombre: ");
                    String nombre = sc.nextLine();
                    System.out.println("Ingrese su nick name");
                    String name = sc.nextLine();
                    try {
                        userRepository.addUser(new User(nombre,name));
                        usuarioActivo = userRepository.findUser(name);
                        System.out.println("Usuario Creado " + usuarioActivo.getNombre());
                        validar = true ;
                    } catch (LibraryException e) {
                        System.err.println("Error al registrar un usuario: "+e.getMessage());
                    }
                }
                default -> System.out.printf("Opcion no reconocida");
            }
            System.out.println("Sistema de biblioteca");
        }while(validar);



    }
}
