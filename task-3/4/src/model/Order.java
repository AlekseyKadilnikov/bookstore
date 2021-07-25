package model;

public class Order extends BaseEntity {
    private Book book;
    private User user;
    private OrderStatus status;

    public Order(Book book, User user) {
        super();
        this.book = book;
        this.user = user;
        status = OrderStatus.New;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
