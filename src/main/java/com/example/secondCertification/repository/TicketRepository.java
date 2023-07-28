package com.example.secondCertification.repository;

import com.example.secondCertification.entity.Ticket;
import org.springframework.data.repository.CrudRepository;

public interface TicketRepository extends CrudRepository<Ticket, Long> {
    Iterable<Ticket> findTicketsByUser_Token(String token);
}
