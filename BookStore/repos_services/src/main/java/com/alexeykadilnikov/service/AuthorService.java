package com.alexeykadilnikov.service;

import com.alexeykadilnikov.dto.AuthorDto;
import com.alexeykadilnikov.entity.Author;
import com.alexeykadilnikov.mapper.AuthorMapper;
import com.alexeykadilnikov.repository.IAuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorService implements IAuthorService {

    private final IAuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(IAuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<AuthorDto> findAll() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorDto> authorsDto = new ArrayList<>();

        for(Author author : authors) {
            authorsDto.add(authorMapper.toDto(author));
        }

        return authorsDto;
    }

    @Override
    public AuthorDto getById(Long id) {
        Optional<Author> author = authorRepository.findById(id);

        if(author.isEmpty()) {
            throw new NullPointerException("Author with id = " + id + " not found");
        }

        return authorMapper.toDto(author.get());
    }

    @Override
    public AuthorDto save(AuthorDto author) {
        return authorMapper.toDto(authorRepository.save(authorMapper.toEntity(author)));
    }

    @Override
    public void delete(AuthorDto author) {
        authorRepository.delete(authorMapper.toEntity(author));
    }
}
