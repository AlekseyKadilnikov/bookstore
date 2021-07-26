package service;

import model.*;
import repository.OrderRepository;
import repository.RequestRepository;

public class OrderService implements IOrderService {
    private OrderRepository orderRepository;
    private RequestRepository requestRepository;

    public OrderService(OrderRepository orderRepository, RequestRepository requestRepository) {
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void createOrder(Book book, User user) {
        if(!book.isAvailable()) {
            requestRepository.save(new Request(book, user));
            System.out.println("Request id = " + requestRepository.getRequest().getId() + " created");
        }
        orderRepository.save(new Order(book, user));
        System.out.println("Order id = " + orderRepository.getOrder().getId() + " created");
    }

    @Override
    public String showOrder() {
        return orderRepository.getOrder().toString();
    }

    @Override
    public void cancelOrder() {
        orderRepository.getOrder().setStatus(OrderStatus.Canceled);
        if(requestRepository.getRequest() != null)
        if(requestRepository.getRequest().getBook() == orderRepository.getOrder().getBook()) {
            requestRepository.getRequest().setStatus(RequestStatus.Closed);
        }
        System.out.println("Order id = " + orderRepository.getOrder().getId() + " canceled");
    }

    @Override
    public void setStatus(OrderStatus status) {
        orderRepository.getOrder().setStatus(status);
    }

    @Override
    public void completeOrder() {
        if(requestRepository.getRequest().getBook() == orderRepository.getOrder().getBook() &&
            requestRepository.getRequest().getStatus() == RequestStatus.Opened) {
            System.out.println("Order id = " + orderRepository.getOrder().getId() + " couldn't be completed: request id = " +
                                                            requestRepository.getRequest().getId() + " not closed");
            return;
        }
        orderRepository.getOrder().setStatus(OrderStatus.Completed);
        System.out.println("Order id = " + orderRepository.getOrder().getId() + " completed");
    }
}
