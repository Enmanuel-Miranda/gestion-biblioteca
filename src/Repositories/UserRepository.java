package Repositories;

import Models.User;
import exceptions.LibraryException;

import java.util.List;

public interface UserRepository {

    void addUser(User user) throws LibraryException;
    void removeUser(String user) throws LibraryException;
    User findUser(String user) throws LibraryException;
    List<User> getAllUser() throws LibraryException;
}
