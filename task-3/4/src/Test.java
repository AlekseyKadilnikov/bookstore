import model.OrderStatus;
import model.User;
import repository.BookRepository;
import repository.OrderRepository;
import repository.RequestRepository;
import repository.UserRepository;
import service.BookService;
import service.OrderService;
import service.RequestService;
import service.UserService;

public class Test {
    public static void main(String[] args) {
        RequestRepository requestRepository = new RequestRepository();
        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        userService.addUser("Alex");

        // Списать книгу со склада (перевести в статус “отсутствует”)
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository, requestRepository);
        System.out.println(bookService.showBook(0));
        bookService.setBookStatus(0, false);
        System.out.println(bookService.showBook(0));

        // Создать заказ
        OrderRepository orderRepository = new OrderRepository();
        OrderService orderService = new OrderService(orderRepository, requestRepository);
        orderService.createOrder(bookRepository.getByIndex(3), userService.getUser());

        // Отменить заказ
        orderService.cancelOrder();

        // Изменить статус заказа (новый, выполнен, отменен)
        orderService.setStatus(OrderStatus.Completed);
        System.out.println(orderService.showOrder());

        // Оставить запрос на книгу
        RequestService requestService = new RequestService(requestRepository);
        requestService.createRequest(bookRepository.getByIndex(1), userService.getUser());

        // Закрыть запрос на книгу
        requestService.cancelRequest();

        // Попробовать создать заказ с отсутствующей книгой и завершить заказ с невыполненным запросом
        orderService.createOrder(bookRepository.getByIndex(1), userService.getUser());
        orderService.completeOrder();

        // Добавить книгу на склад (закрывает все запросы книги и меняет ее статус на “вналичии”)
        bookService.setBookStatus(1, true);

        // Попробовать еще раз завершить заказ (запрос уже выполнен)
        orderService.completeOrder();
    }
}
