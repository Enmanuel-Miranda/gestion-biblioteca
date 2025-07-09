package Repositories;

import Models.User;
import exceptions.LibraryException;

import java.util.ArrayList;
import java.util.List;

public class ArrayListUserRepository implements UserRepository {

    private List<User> users;
    
    //Constructor 
    public ArrayListUserRepository () {
        this.users = new ArrayList<>();
    }
    
    @Override
    public void addUser(User user) throws LibraryException {
        //Validaccion de que el usuario no se repita
        for(User existe : users){
            if(existe.getUsuario().equals(user.getUsuario())) {
                throw new LibraryException("El usuario "+user.getUsuario()+" ya existe");
            }
        }
        users.add(user); //Agrega Usuario
    }

    @Override
    public void removeUser(String user) throws LibraryException {
        User removeUsuario = findUser(user);
        users.remove(removeUsuario);
    }

    @Override
    public User findUser(String user) throws LibraryException {
        //Buscar por usuario
        for(User encontrar :users) {
            if(encontrar.getUsuario().equals(user)){
                return encontrar;
            }
        }
        throw new LibraryException("No se encontro el usuario con en nombre "+user);
    }

    @Override
    public List<User> getAllUser() throws LibraryException {
        //Retorna una copia
        return new ArrayList<>(users);
    }
}
