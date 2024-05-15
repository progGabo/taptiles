package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Users;

public interface UserService {

    Users getUser(String login);
    boolean addUser(Users user);

    void reset();
}
