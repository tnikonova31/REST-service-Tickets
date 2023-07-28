package com.example.secondCertification.controllers;

import com.example.secondCertification.entity.Ticket;
import com.example.secondCertification.models.TicketModel;
import com.example.secondCertification.models.UserModel;
import com.example.secondCertification.models.answers.ApiAnswer;
import com.example.secondCertification.models.answers.ViewJSON;
import com.example.secondCertification.service.TicketService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/tickets")
@Tag(name="Залоговые билеты", description="Выполняет действия с залоговыми билетами")
public class TicketController {
    private final TicketService ticketService;


    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /** Вывод всех залоговых билетов
     *
     * @return
     */
    @GetMapping(value = "")
    @JsonView(ViewJSON.SummaryTickets.class)
    @Operation(
            summary = "Получение всех залоговых билетов",
            description = "Просмотр залоговых билетов с ограниченной информацией по пользователям"
    )
    public ResponseEntity<Iterable<Ticket>> read(){
        Iterable<Ticket> tickets = ticketService.readAll();
        return tickets != null
                ? new ResponseEntity<>(tickets, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /** Создание нового залогового билета
     *
     * @param ticket данные для создания билета
     * @return
     */
    @PostMapping(value = "/create/{token}")
    @Operation(
            summary = "Создание залогового билета"
    )
    public ResponseEntity<?> create(@PathVariable (value = "token") @Parameter(description = "Токен пользователя")  String token,
                                    @RequestBody Ticket ticket,
                                    HttpServletRequest request){
        HttpSession session = request.getSession();
        String tokenInSession = (String) session.getAttribute("userToken");
        if (token ==null || token.isEmpty()) { //если токена нет или пустая строка
            return TicketModel.badToken();
        } else if(!token.equals((tokenInSession))){
            return TicketModel.missingToken();
        } else if (ticket.getPrice() <=0 || ticket.getDateCreated() ==null || ticket.getDateClosing() ==null){
            return TicketModel.badTicket();
        }else if (ticketService.isCreate(token, ticket)){ // если билет создан
            return UserModel.createEntity("Ticket");
        }
        return UserModel.entityNotCreate("Ticket");
    }

    /** Пролонгация билета (только дата закрытия)
     *
     * @param ticketId билет, который ищем
     * @param ticket новые данные
     * @return
     */
    @PutMapping(value = "/update/{token}&{id}")
    @Operation(
            summary = "Пролонгация залогового билета",
            description = "Пролонгация осуществляется по дате закрытия или по количеству дней, на которое необходимо продлить"
    )
    public ResponseEntity<?> update(@PathVariable(name="id") @Parameter(description = "ID пользователя") long ticketId,
                                    @PathVariable(value = "token") @Parameter(description = "Токен пользователя") String token,
                                    @RequestBody Ticket ticket,
                                    HttpServletRequest request){
        HttpSession session = request.getSession();
        String tokenInSession = (String) session.getAttribute("userToken");
        if (token ==null || token.isEmpty()) { //если токена нет или пустая строка
            return TicketModel.badToken();
        } else if(!token.equals((tokenInSession))) {
            return TicketModel.missingToken();
        } else if(ticketService.isUpdateDateClosing(token, ticket, ticketId)){
            return TicketModel.updateTicket();
        }
        return TicketModel.ticketNotUpdate();
    }

    /** Поиск всех билетов пользователя
     *
     * @param token
     * @param request
     * @return
     */
    @GetMapping(value="/find/{token}")
    @JsonView(ViewJSON.TicketsForUser.class)
    @Operation(
            summary = "Получение залоговых билетов конкретного пользователя"
    )
    public ResponseEntity<?> findUserTickets(@PathVariable(value = "token") @Parameter(description = "Токен пользователя") String token,
                                                     HttpServletRequest request){
        HttpSession session = request.getSession();
        String tokenInSession = (String) session.getAttribute("userToken");
        if (token ==null || token.isEmpty()) { //если токена нет или пустая строка)
            return TicketModel.badToken();
        } else if(!token.equals((tokenInSession))) { // если в сессии нет токена
            return TicketModel.missingToken();
        } else{
            Iterable<Ticket> tickets = ticketService.readForUser(token);
            return tickets != null ?
                    new ResponseEntity<>(tickets, HttpStatus.OK):
                    UserModel.notFind("Tickets for this token");
        }
    }

    /** Удаление залогового билета
     *
     * @param ticketId
     * @param token
     * @param request
     * @return
     */
    @DeleteMapping(value = "/delete/{token}&{id}")
    @Operation(
            summary = "Удаление залогового билета пользователя"
    )
    public ResponseEntity<?> delete (@PathVariable(name="id") @Parameter(description = "ID пользователя") long ticketId,
                                     @PathVariable(value = "token") @Parameter(description = "Токен пользователя") String token,
                                    HttpServletRequest request){
        HttpSession session = request.getSession();
        String tokenInSession = (String) session.getAttribute("userToken");
        if (token ==null || token.isEmpty()) { //если токена нет или пустая строка)
            return TicketModel.badToken();
        } else if(!token.equals((tokenInSession))) { // если в сессии нет токена
            return TicketModel.missingToken();
        } else if(ticketService.isDelete(token, ticketId)){
            return TicketModel.ticketDelete();
        }
        return TicketModel.ticketNotDelete();
    }
}
