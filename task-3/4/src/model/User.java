package model;

public class User {
    private int id;
    private String username;
    private Order[] orders;
    private Book[] books;
    private Request[] requests;

    public User(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Order[] getOrders() {
        return orders;
    }

    public void newOrder(Order order) {
        /////////
    }

    public Book[] getBooks() {
        return books;
    }

    public void addBook(Book book) {
        /////////
    }

    public Request[] getRequests() {
        return requests;
    }

    public void newRequest(Request[] requests) {
        /////////
    }
}
