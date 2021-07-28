package service;

import model.RequestStatus;
import repository.BookRepository;
import repository.RequestRepository;

public class BookService implements IBookService {
    private final BookRepository bookRepository;
    private final RequestRepository requestRepository;

    public BookService(BookRepository bookRepository, RequestRepository requestRepository) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void setBookStatus(int index, boolean status) {
        if(!bookRepository.getByIndex(index).isAvailable() && status) {
            requestRepository.getRequest().setStatus(RequestStatus.Closed);
            System.out.println("Request id = " + requestRepository.getRequest().getId() + " closed");
        }
        bookRepository.getByIndex(index).setAvailable(status);
    }

    @Override
    public String showBook(int index) {
        return bookRepository.getByIndex(index).toString();
    }
}
