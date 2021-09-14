package com.alexeykadilnikov.entity;

import com.alexeykadilnikov.RequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "request")
public class Request extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 4499720100063137694L;
    @Column(name = "name")
    private String name;
    @Column(name = "count")
    private int count;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status_code")
    private RequestStatus status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_request",
            joinColumns = @JoinColumn(name="request_id"),
            inverseJoinColumns = @JoinColumn(name="book_id")
    )
    private Set<Book> books = new HashSet<>();

    public Request(String name, int count, RequestStatus status, Set<Book> books) {
        this.name = name;
        this.count = count;
        this.status = status;
        this.books = books;
    }

    @Override
    public String toString() {
        return "Request{" +
                "name='" + name + '\'' +
                ", count=" + count + ", status = " + status.name() +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return count == request.count && name.equals(request.name) &&
                status == request.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, count, status);
    }
}
