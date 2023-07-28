package com.example.secondCertification.service;

import com.example.secondCertification.entity.Ticket;

public interface TicketService {
    /** Создание залогового билета
     *
     * @param token
     * @param ticket
     * @return
     */
    boolean isCreate(String token, Ticket ticket);

    /** Получение всех билетов
     *
     * @return
     */
    Iterable<Ticket> readAll();

    /** Получение билетов конкретного пользователя
     *
     * @param token
     * @return
     */
    Iterable<Ticket> readForUser(String token);

    /** Обновление даты билета
     *
     * @param ticket
     * @param id
     * @return
     */
    boolean isUpdateDateClosing(String token, Ticket ticket, Long id);

    boolean isDelete(String token, Long id);

}
