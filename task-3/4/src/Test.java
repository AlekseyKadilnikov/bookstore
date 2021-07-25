import model.Book;
import model.User;
import service.BookService;
import service.UserService;

public class Test {
    public static void main(String[] args) {

        Book[] books = new Book[] {
            new Book("name1", "author1", "publisher1", 200, true),
            new Book("name2", "author2", "publisher2", 300, false),
            new Book("name3", "author3", "publisher1", 200, false),
            new Book("name4", "author4", "publisher2", 500, true),
            new Book("name5", "author5", "publisher1", 600, true)
        };

        BookService bookService = new BookService(books);
        User user = new User("Alex");
        UserService userService = new UserService(user);

        userService.createOrder(bookService.findById(0));

    }
}
