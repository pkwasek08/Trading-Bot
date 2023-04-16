package pl.project.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<UsersEntity> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public UsersEntity getUser(@PathVariable Integer id) {
        return userService.getUser(id);
    }

    @PostMapping()
    public void addUser(@RequestBody UsersEntity user) {
        userService.addUser(user);
    }

    @PutMapping(value = "/{id}")
    public void updateUser(@RequestBody UsersEntity user, @PathVariable Integer id) {
        userService.updateUser(id, user);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}
