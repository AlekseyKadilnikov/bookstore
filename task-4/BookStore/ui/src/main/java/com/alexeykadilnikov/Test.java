package com.alexeykadilnikov;

import com.alexeykadilnikov.entity.Book;
import com.alexeykadilnikov.entity.Order;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.repository.BookRepository;
import com.alexeykadilnikov.repository.OrderRepository;
import com.alexeykadilnikov.repository.RequestRepository;
import com.alexeykadilnikov.repository.UserRepository;
import com.alexeykadilnikov.service.BookService;
import com.alexeykadilnikov.service.OrderService;
import com.alexeykadilnikov.service.RequestService;
import com.alexeykadilnikov.service.UserService;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Test {
    public static void main(String[] args) {
        RequestRepository requestRepository = new RequestRepository();
        RequestService requestService = new RequestService(requestRepository);
        BookRepository bookRepository = new BookRepository();
        BookService bookService = new BookService(bookRepository, requestRepository);

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        userService.addUser("Alex");

        OrderRepository orderRepository = new OrderRepository();
        OrderService orderService = new OrderService(orderRepository, requestRepository);

        System.out.println("------------------------------СОРТИРОВКА КНИГ-----------------------------------\n");

        System.out.println("Сортировка книг по имени по убыванию:\n");
        Book[] books = bookService.sortByNameDescending(bookService.getAll().clone());
        showBookArray(books);

        System.out.println("Сортировка книг по цене по убыванию:\n");
        books = bookService.sortByPriceDescending(bookService.getAll().clone());
        showBookArray(books);

        System.out.println("Сортировка книг по дате издания по убыванию:\n");
        books = bookService.sortByYearDescending(bookService.getAll().clone());
        showBookArray(books);

        System.out.println("Сортировка книг по наличию на складе по убыванию:\n");
        books = bookService.sortByAvailableDescending(bookService.getAll().clone());
        showBookArray(books);

        System.out.println("Список «залежавшихся» книг не проданы больше чем 6 мес.");
        Calendar calendar = new GregorianCalendar(2020, Calendar.AUGUST, 21);
        bookService.getByIndex(4).setDateOfReceipt(calendar.getTime());
        calendar = new GregorianCalendar(2020, Calendar.FEBRUARY, 10);
        bookService.getByIndex(3).setDateOfReceipt(calendar.getTime());
        calendar = new GregorianCalendar(2021, Calendar.JANUARY, 1);
        bookService.getByIndex(1).setDateOfReceipt(calendar.getTime());

        System.out.println("По дате поступления по убыванию:\n");
        books = bookService.getOldBooks(6);
        books = bookService.sortByDateOfReceiptDescending(books);
        showBookArray(books);

        System.out.println("По цене по убыванию:\n");
        books = bookService.sortByPriceDescending(books);
        showBookArray(books);

        System.out.println("---------------------------СОРТИРОВКА ЗАКАЗОВ--------------------------------\n");

        System.out.println("Создание заказов:\n");
        Book book1 = bookService.getByIndex(0);
        Book book2 = bookService.getByIndex(1);
        orderService.createOrder(new Book[] {book1}, userService.getByIndex(0));
        orderService.createOrder(new Book[] {book1, book2}, userService.getByIndex(0));
        orderService.createOrder(new Book[] {book1, book1}, userService.getByIndex(0));
        System.out.println();

        System.out.println("Сортировка заказов по дате исполнения по убыванию:\n");
        Order[] orders = orderService.sortByExecutionDateDescending(orderService.getAll().clone());
        showOrderArray(orders);

        System.out.println("Сортировка заказов по цене по убыванию:\n");
        orders = orderService.sortByPriceDescending(orderService.getAll().clone());
        showOrderArray(orders);

        orderService.setStatus(0, OrderStatus.COMPLETED);
        orderService.setStatus(1, OrderStatus.CANCELED);

        System.out.println("Сортировка заказов по статусу по возрастанию:\n");
        orders = orderService.sortByStatusAscending(orderService.getAll().clone());
        showOrderArray(orders);
        System.out.println("Сортировка заказов по статусу по убыванию:\n");
        orders = orderService.sortByStatusDescending(orderService.getAll().clone());
        showOrderArray(orders);

        orderService.createOrder(new Book[] {book1}, userService.getByIndex(0));
        orderService.createOrder(new Book[] {book1, book2}, userService.getByIndex(0));
        orderService.createOrder(new Book[] {book1, book1}, userService.getByIndex(0));

        System.out.println();

        orderService.setStatus(2, OrderStatus.COMPLETED);
        orderService.setStatus(3, OrderStatus.COMPLETED);
        orderService.setStatus(4, OrderStatus.COMPLETED);
        orderService.setStatus(5, OrderStatus.COMPLETED);

        System.out.println("Всего заказов:\n");
        showOrderArray(orderService.getAll());
        Calendar calendarAfter = new GregorianCalendar(2021, Calendar.AUGUST , 1);
        Calendar calendarBefore = new GregorianCalendar(2021, Calendar.AUGUST , 5);

        System.out.println("Выполненных заказов от 1.08.2021 до 04.08.2021:\n");
        orders = orderService.getOrderListForPeriod(calendarAfter.getTime(), calendarBefore.getTime());
        showOrderArray(orders);
        System.out.println("Сортировка по дате по убыванию:\n");
        orders = orderService.sortByExecutionDateDescending(orders);
        showOrderArray(orders);
        System.out.println("Сортировка по цене по убыванию:\n");
        orders = orderService.sortByPriceDescending(orders);
        showOrderArray(orders);

        int sum = orderService.getAmountOfMoneyForPeriod(calendarAfter.getTime(), calendarBefore.getTime());
        System.out.println("Сумма заработанных средств от 1.08.2021 до 04.08.2021 = " + sum + " руб.");

        int count = orderService.getAmountOfCompletedOrdersForPeriod(calendarAfter.getTime(), calendarBefore.getTime());
        System.out.println("Выполненных заказов от 1.08.2021 до 04.08.2021 = " + count + " шт.");

        System.out.println();

        System.out.println("---------------------------СОРТИРОВКА ЗАПРОСОВ------------------------------\n");
        book1.setAvailable(false);
        userService.addUser("Bob");
        userService.addUser("Arnold");
        requestService.createRequest(book1, userService.getByIndex(0));
        requestService.createRequest(book1, userService.getByIndex(0));
        requestService.createRequest(book1, userService.getByIndex(0));
        requestService.createRequest(book1, userService.getByIndex(1));
        requestService.createRequest(book1, userService.getByIndex(2));
        requestService.createRequest(book1, userService.getByIndex(2));

        System.out.println();

        System.out.println("Сортировка запросов на книгу по количеству запросов по убыванию:\n");
        Request[] requests = requestService.sortByAmountDescending(book1, requestService.getAll().clone());
        showRequestArray(requests);

        book1.setDescription("Описание book1");
        System.out.println("Посмотреть описание книги:");
        System.out.println(bookService.getBookDescription(book1));

        System.out.println();

        System.out.println("Посмотреть детали заказа:");
        System.out.println(orderService.showOrder(1));
    }

    private static void showBookArray(Book[] books) {
        for (Book book : books) {
            System.out.println("name = " + book.getName() +
                    ", price = " + book.getPrice() +
                    ", date = " + book.getPublicationYear() +
                    ", available = " + book.isAvailable() +
                    ", dateOfReceipt = " + book.getDateOfReceipt());
        }
        System.out.println();
    }

    private static void showOrderArray(Order[] orders) {
        for (Order order : orders) {
            System.out.println("id = " + order.getId() +
                    ", price = " + order.getPrice() +
                    ", date = " + order.getExecutionDate().getTime() +
                    ", status = " + order.getStatus());
        }
        System.out.println();
    }

    private static void showRequestArray(Request[] requests) {
        for (Request request : requests) {
            System.out.println("id = " + request.getId() +
                    ", book = " + request.getBook() +
                    ", user = " + request.getUser() +
                    ", amount = " + request.getAmount());
        }
        System.out.println();
    }
}
