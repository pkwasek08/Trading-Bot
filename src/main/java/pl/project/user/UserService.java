package pl.project.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<UsersEntity> getAllUser() {
        List<UsersEntity> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);
        return users;
    }

    public UsersEntity getUser(Integer id) {
        return userRepository.findById(id).get();
    }

    public void addUser(UsersEntity user) {
        userRepository.save(user);
    }


    public void updateUser(Integer id, UsersEntity user) {
        userRepository.save(user);
    }


    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}
