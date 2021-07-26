package repository;

import model.Order;

import java.util.List;

public class OrderRepository implements IRepository<Order, Long> {
    private Order order;

    public OrderRepository() {
    }

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
        this.order = order;
    }

    @Override
    public void delete(Order order) {

    }

    public Order getOrder() {
        return order;
    }
}
