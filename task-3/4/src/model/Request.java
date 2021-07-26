package model;

public class Request extends BaseEntity {
    private static long ID_COUNT = 0;
    private Book book;
    private User user;
    private RequestStatus status;

    public Request(Book book, User user) {
        super(ID_COUNT++);
        this.book = book;
        this.user = user;
        status = RequestStatus.Opened;
    }

    @Override
    public String toString() {
        return "Request{" +
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

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }
}
