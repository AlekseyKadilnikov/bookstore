package com.alexeykadilnikov.service;

import com.alexeykadilnikov.RequestStatus;
import com.alexeykadilnikov.dto.RequestDto;
import com.alexeykadilnikov.entity.Request;
import com.alexeykadilnikov.mapper.RequestMapper;
import com.alexeykadilnikov.repository.IRequestRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RequestServiceTest {
    private IRequestService requestService;

    @MockBean
    private IRequestRepository requestRepository;

    long requestId = 1L;

    @BeforeAll
    void init(){

        RequestMapper requestMapper = new RequestMapper(new ModelMapper());

        requestService = new RequestService(requestRepository, requestMapper);
    }

    @Test
    void shouldFetchAllBooks() {
        List<Request> testRequests = new ArrayList<>();
        testRequests.add(new Request("r", 1, RequestStatus.COMMON));
        testRequests.add(new Request("r1", 1, RequestStatus.COMMON));
        testRequests.add(new Request("r2", 1, RequestStatus.COMMON));

        List<RequestDto> testRequestsDto = new ArrayList<>();
        testRequestsDto.add(new RequestDto("r"));
        testRequestsDto.add(new RequestDto("r1"));
        testRequestsDto.add(new RequestDto("r2"));

        given(requestRepository.findAll()).willReturn(testRequests);

        List<RequestDto> result = requestService.getAll();

        Assertions.assertIterableEquals(testRequestsDto, result);
    }

    @Test
    void shouldFetchOneBookById() {
        Request request = new Request("r", 1, RequestStatus.COMMON);
        request.setId(requestId);

        given(requestRepository.findById(1L)).willReturn(Optional.of(request));

        RequestDto requestDto = requestService.getById(requestId);

        Assertions.assertEquals(requestId, requestDto.getId());
    }
}
