package model;

public class User extends BaseEntity {
    private static long ID_COUNT = 0;
    private String username;
    private Order order;
    private Request request;

    public User(String username) {
        super(ID_COUNT++);
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", order=" + order +
                ", request=" + request +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Order getOrder() {
        return order;
    }

    public Request getRequest() {
        return request;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}