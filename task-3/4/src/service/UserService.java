package service;

import model.User;
import repository.UserRepository;

public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(String username) {
        userRepository.save(new User(username));
    }

    @Override
    public User getUser() {
        return userRepository.getUser();
    }


}
