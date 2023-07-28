package com.example.secondCertification.entity;

import com.example.secondCertification.models.answers.ViewJSON;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@javax.persistence.Table(name="tickets")
@Data
@NoArgsConstructor
@Schema(description = "Сущность залогового билета")
public class Ticket {
    @JsonView(ViewJSON.TicketsForUser.class)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Номер договора / ID билета")
    private long ticketId;

    @JsonView(ViewJSON.TicketsForUser.class)
    @Schema(description = "Сумма залога")
    private int price;
    @JsonView(ViewJSON.TicketsForUser.class)
    @Schema(description = "Дата создания залогового билета")
    private String dateCreated;

    @JsonView(ViewJSON.TicketsForUser.class)
    @Schema(description = "Дата закрытия залогового билета")
    private String dateClosing;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") //, updatable=false, nullable=false
    @JsonView(ViewJSON.SummaryTickets.class)
    @Schema(description = "Клиент, которому принадлежит залоговый билет")
    private User user;

    @Transient
    @Schema(description = "Количество дней для продления даты закрытия залогового билета")
    private int dayIncrease; // количество дней для продоения залогового билета, в БД не учитывается

    public Ticket(int price, String dateCreated, String dateClosing) {
        this.price = price;
        this.dateCreated = dateCreated;
        this.dateClosing = dateClosing;
    }
}
