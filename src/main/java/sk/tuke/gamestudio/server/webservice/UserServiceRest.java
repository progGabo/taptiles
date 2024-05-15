package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Users;
import sk.tuke.gamestudio.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    @GetMapping("/{login}")
    public Users getUser(@PathVariable String login){
        return userService.getUser(login);
    }

    @PostMapping
    public void addUser(@RequestBody Users user){
        userService.addUser(user);
    }
}
