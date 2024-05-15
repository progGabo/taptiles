package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Users;
import sk.tuke.gamestudio.game.GameCore.Field;
import sk.tuke.gamestudio.service.UserService;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {
    private Users loggedUser;
    @Autowired
    private UserService userServiceR;

    private boolean correctPass = true;
    private boolean registeredUser = true;

    @PostMapping("/guest")
    public String joinAsGuest(){
        return "redirect:/taptiles/random/easy";
    }

    @PostMapping("/login")
    public String login(String login, String password){
        if(userServiceR.getUser(login) != null) {
            if (correctPassword(login, password)) {
                loggedUser = userServiceR.getUser(login);
                return "redirect:/taptiles/random/easy";
            }
        }
        return "redirect:/";
    }


    public boolean correctPassword(String login, String password) {
        if(userServiceR.getUser(login).getPassword().equals(password)) {
            correctPass = true;
            return true;
        }
        correctPass = false;
        return false;
    }


    @PostMapping("/register")
    public String register(String login, String password){
        if(userRegistered(login)){
            userServiceR.addUser(new Users(login,password));
            return "redirect:/";
        }
        return "redirect:/";
    }

    public boolean userRegistered(String login){
        if(userServiceR.getUser(login) == null){
            registeredUser = true;
            return true;
        }
        registeredUser = false;
        return false;
    }

    public void logoutUser(){
        loggedUser = null;
    }


    public String getUserName(){
        if (isLogged()) return loggedUser.getLogin();
        return "Guest";
    }

    public Users getLoggedUser() {
        return loggedUser;
    }

    public boolean isLogged() {
        return loggedUser != null;
    }

    public boolean isRegisteredUser() {
        return registeredUser;
    }

    public boolean isCorrectPass() {
        return correctPass;
    }

    public void setLoggedUser(Users loggedUser) {
        this.loggedUser = loggedUser;
    }
}
