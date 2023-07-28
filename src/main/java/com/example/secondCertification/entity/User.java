package com.example.secondCertification.entity;

import com.example.secondCertification.models.answers.ViewJSON;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@Schema(description = "Сущность пользователя")

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID пользователя")
    private long userId;
    @JsonView(ViewJSON.SummaryTickets.class)
    @Schema(description = "Логин пользователя")
    private String login;
    @JsonView(ViewJSON.SummaryTickets.class)
    @Schema(description = "Телефон пользователя")
    private String phone;
    @Schema(description = "Пароль пользователя")
    private String password;
    @Schema(description = "Токен, генерируется при авторизации")
    private String token;

    public User(String login, String phone, String password) {
        this.login = login;
        this.phone = phone;
        this.password=password;
    }
}
