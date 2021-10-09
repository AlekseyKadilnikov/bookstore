package com.alexeykadilnikov.repository;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.config.MySql;
import com.alexeykadilnikov.entity.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration(initializers = {
        MySql.Initializer.class
})
class RequestRepositoryTest {
    @Autowired
    private IRequestRepository requestRepository;

    @BeforeAll
    static void init() {
        MySql.container.start();
    }

    @Test
    void saveRequest_and_getRequestById() {
        requestRepository.deleteAll();

        Request request = new Request();
        request.setCount(1);
        request.setStatus(RequestStatus.NEW);
        request.setName("name");

        Request returnedRequest = requestRepository.save(request);

        Optional<Request> returned = requestRepository.findById(returnedRequest.getId());

        Assertions.assertTrue(returned.isPresent());
        Assertions.assertEquals(returned.get().getId(), request.getId());
    }

    @Test
    void saveRequest_and_deleteRequest() {
        requestRepository.deleteAll();

        Request request = new Request();
        request.setCount(1);
        request.setStatus(RequestStatus.NEW);
        request.setName("name");

        requestRepository.save(request);
        requestRepository.delete(request);

        int size = requestRepository.findAll().size();

        Assertions.assertEquals(0, size);
    }

    @Test
    void saveRequest_and_findAllRequests() {
        requestRepository.deleteAll();

        Request requestOne = new Request("email", 1, RequestStatus.COMMON);
        Request requestTwo = new Request("email", 1, RequestStatus.COMMON);
        requestRepository.save(requestOne);
        requestRepository.save(requestTwo);

        List<Request> requests = requestRepository.findAll();

        Assertions.assertEquals(2, requests.size());
    }

    @Test
    void save_and_updateRequest() {
        requestRepository.deleteAll();

        Request request = new Request();
        request.setCount(1);
        request.setStatus(RequestStatus.NEW);
        request.setName("name");

        Request returned = requestRepository.save(request);

        Assertions.assertNotNull(returned);

        long idBefore = returned.getId();

        request.setName("renamed");
        returned = requestRepository.save(request);

        Assertions.assertNotNull(returned);

        long idAfter = returned.getId();
        String nameAfter = returned.getName();

        Assertions.assertEquals(idBefore, idAfter);
        Assertions.assertEquals(request.getName(), nameAfter);
    }
}
