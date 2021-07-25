package repository;

import model.Order;

import java.util.List;

public class OrderRepository implements IRepository<Order, Long> {
    private Order order;

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order getById(Long id) {
        return null;
    }

    @Override
    public void save(Order order) {

    }

    @Override
    public void delete(Order order) {

    }
}
