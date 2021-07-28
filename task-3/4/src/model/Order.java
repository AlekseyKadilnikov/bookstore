package model;

public class Order extends BaseEntity {
    private static long ID_COUNT = 0;
    private Book book;
    private User user;
    private OrderStatus status;

    public Order(Book book, User user) {
        super(ID_COUNT++);
        this.book = book;
        this.user = user;
        status = OrderStatus.New;
    }

    @Override
    public String toString() {
        return "Order{" +
                "book=" + book +
                ", user=" + user +
                ", status=" + status +
                '}';
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
