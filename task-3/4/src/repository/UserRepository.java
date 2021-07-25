package repository;

import model.User;

import java.util.List;

public class UserRepository implements IRepository<User, Long>{
    private User user;

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public User getById(Long id) {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void delete(User user) {

    }
}
