package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.io.Serializable;

@Entity
@NamedQuery( name = "User.resetUsers",
        query = "DELETE FROM Users")
@NamedQuery(name = "User.findUser",
        query = "SELECT u FROM Users u WHERE u.login=:login")
public class Users implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private String login;
    private String password;


    public Users(String login, String password){
        this.login = login;
        this.password = password;
    }

    public Users(){

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;

    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
