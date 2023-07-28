package com.example.secondCertification.service;

import com.example.secondCertification.entity.Ticket;
import com.example.secondCertification.entity.User;
import com.example.secondCertification.models.TicketModel;
import com.example.secondCertification.repository.TicketRepository;
import com.example.secondCertification.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class TicketServiceImpl implements TicketService{
    private static final AtomicLong TICKET_ID = new AtomicLong();
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    UserRepository userRepository;

    /** Получение всех билетов
     *
     * @return
     */
    @Override
    public Iterable<Ticket> readAll() {
        return ticketRepository.findAll();
    }

    /** Получение билетов конкретного пользователя
     *
     * @param token
     * @return
     */
    @Override
    public Iterable<Ticket> readForUser(String token) {
        return ticketRepository.findTicketsByUser_Token(token);
    }

    /** Создание нового залогового билета
     *
     * @param ticket данные билета
     * @return true - данные верные, билет создан; false - что-то пошло не так
     */
    @Override
    public boolean isCreate(String token, Ticket ticket) {
            if(TicketModel.isCurrentDate(ticket.getDateCreated(), ticket.getDateClosing())){
                User userFind = userRepository.findUserByToken(token);
                if(userFind != null){
                    Long ticketId = TICKET_ID.incrementAndGet(); // получаем ID
                    Ticket ticketFind = ticketRepository.findById(ticketId).orElse(null); // ищем по ID в БД
        /* Без проверки ID в базе метод incrementAndGet() начинает генерировать ID c 1
        и перезаписывает всех пользователей, которые уже есть в БД*/
                    while (ticketFind != null) { // пока находим с таким ID
                        ticketId = TICKET_ID.incrementAndGet(); // генерируем новый ID
                        ticketFind = ticketRepository.findById(ticketId).orElse(null); // проверяем
                    }
                    ticket.setTicketId(ticketId); //зафиксировали ID
                    ticket.setUser(userFind); // связали билет с user
                    ticketRepository.save(ticket); // сохранили билет
                    return true;
                }
            }
        return false;
    }

    /** Пролонгация даты закрытия билета,
     * без изменения других параметров
     *
     * @param ticket данные для изменения
     * @param id билета для измененя
     * @return true - данные верны, всё успешно, false - что-то пошло не так
     */
    @Override
    public boolean isUpdateDateClosing(String token, Ticket ticket, Long id) {
        Ticket ticketFind = ticketRepository.findById(id).orElse(null);
        if(ticketFind!= null){
            // если токен user найденного билета совпадает с токеном user, который пытается его удалить
            if(ticketFind.getUser().getToken().equals(token)){
                if (ticket.getDayIncrease() > 0){ // если пришло количество дней для продления
                    String newDate = TicketModel.increaseDate(ticketFind.getDateClosing(), ticket.getDayIncrease());
                    if (!newDate.isEmpty()){
                        ticketFind.setDateClosing(newDate);
                        ticketRepository.save(ticketFind);
                        return true;
                    }
                } else if (ticket.getDateClosing() != null){ // проверяем указана ли дата закрытия
                    if(TicketModel.isCurrentDate(ticketFind.getDateClosing(), ticket.getDateClosing())){
                        ticketFind.setDateClosing(ticket.getDateClosing()); // обновляем только дату
                        ticketRepository.save(ticketFind);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /** Удаление билета
     *
     * @param token
     * @param id
     * @return
     */
    @Override
    public boolean isDelete(String token, Long id) {
        Ticket ticketFind = ticketRepository.findById(id).orElse(null);
        if(ticketFind!= null){
            if(ticketFind.getUser().getToken().equals(token)){
                ticketRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
